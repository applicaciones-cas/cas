/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.stock.request;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.cas.inventory.stock.Inv_Request_MC;
import org.guanzon.cas.inventory.stock.Inv_Request_SP;
/**
 *
 * @author User
 */
public class RequestControllerFactory {
    public enum RequestType {
        ASSET,
        MC,
        MP,
        SP,
        SUPPLIES
    }
    public static RequestController make(RequestType foType, GRider oApp, boolean fbVal){
        switch (foType) {
            case MC:
                return (RequestController) new Inv_Request_MC(oApp, fbVal);
            case SP:
                return (RequestController) new Inv_Request_SP(oApp, fbVal);
            default:
                return null;
        }
    }
}
