package net.pricefx.connector.common.operation;

import net.pricefx.connector.common.connection.PFXOperationClient;

import static net.pricefx.connector.common.util.PFXTypeCode.ACTION;


public class ActionItemUpdater extends ActionUpdater {

    public ActionItemUpdater(PFXOperationClient pfxClient) {
        super(pfxClient, ACTION);
    }

}