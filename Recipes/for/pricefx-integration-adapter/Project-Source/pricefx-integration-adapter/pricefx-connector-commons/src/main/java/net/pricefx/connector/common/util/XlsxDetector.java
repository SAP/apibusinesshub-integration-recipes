package net.pricefx.connector.common.util;

import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.io.LookaheadInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.OfflineContentHandler;
import org.apache.tika.utils.XMLReaderUtils;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.HTTP_INTERNAL_ERROR;

public class XlsxDetector implements Detector {

    public MediaType detect(InputStream input, Metadata metadata) throws IOException {
        if (input == null) {
            throw new RequestValidationException("Input stream is null");
        }

        int markLimit = 16777216;
        byte[] prefix = new byte[1024];
        input.mark(1024);
        int length;
        try {
            length = IOUtils.read(input, prefix);
        } finally {
            input.reset();
        }

        try {
            String name = ArchiveStreamFactory.detect(new ByteArrayInputStream(prefix, 0, length));
            if (!"zip".equals(name)) {
                return null;
            }
        } catch (ArchiveException ex) {
            return null;
        }

        try (LookaheadInputStream lookahead = new LookaheadInputStream(input, markLimit)) {
            return MediaType.parse(detect(lookahead));
        } catch (Exception ex) {
            return null;
        }
    }

    private static String detect(InputStream is) {
        try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new CloseShieldInputStream(is))) {
            ZipArchiveEntry zae = zipArchiveInputStream.getNextZipEntry();
            if (zae != null && "[Content_Types].xml".equals(zae.getName())) {
                return parseOOXMLContentTypes(zipArchiveInputStream);
            }
        } catch (Exception ex) {
            throw new ConnectorException(HTTP_INTERNAL_ERROR, "cannot detect media type");
        }
        return null;
    }

    private static String parseOOXMLContentTypes(InputStream is) {
        try {
            ContentTypeHandler contentTypeHandler = new ContentTypeHandler();
            XMLReaderUtils.parseSAX(is, new OfflineContentHandler(contentTypeHandler), new ParseContext());
            return contentTypeHandler.getMediaType();
        } catch (Exception ex) {
            return null;
        }
    }

    private static class ContentTypeHandler extends DefaultHandler {
        private String mediaType = null;

        private ContentTypeHandler() {
        }

        private String getMediaType() {
            return mediaType;
        }

        private void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attrs) {
            for (int i = 0; (mediaType == null && i < attrs.getLength()); ++i) {
                String attrName = attrs.getLocalName(i);
                if (attrName.equals("ContentType")) {
                    String contentType = attrs.getValue(i);
                    if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml".equalsIgnoreCase(contentType)) {
                        setMediaType(FileUtil.MediaType.XLSX.getType());
                    }
                }
            }
        }
    }
}
