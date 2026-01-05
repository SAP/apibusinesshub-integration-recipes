package net.pricefx.connector.common.operation;

import net.pricefx.connector.common.connection.PFXOperationClient;

import static net.pricefx.connector.common.util.PFXTypeCode.ACTION_PLAN;


public class ActionPlanUpdater extends ActionUpdater {

    public ActionPlanUpdater(PFXOperationClient pfxClient) {
        super(pfxClient, ACTION_PLAN);
    }

}