package net.pricefx.connector.common.operation;

import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.FileUtil;
import net.pricefx.connector.common.util.PFXOperation;


public class ProductImageUploader extends GenericFileUploader {

    public ProductImageUploader(PFXOperationClient pfxClient, String uniqueId) {
        super(pfxClient, PFXOperation.PRODUCT_IMAGE_UPLOAD, FileUtil.MediaType.IMAGE, "upload.png",
                okhttp3.MediaType.parse(FileUtil.MediaType.IMAGE.getType() + "/png"), uniqueId, "image");
    }


}