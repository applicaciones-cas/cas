/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.resultSet2XML;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;

/**
 *
 * @author User
 */
public class ModelInventoryStockRequestCancelDetail {
    public static void main (String [] args){
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);

        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        System.setProperty("sys.table", "Inv_Stock_Req_Cancel_Detail");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECT" +
                            "  a.sTransNox" +
                            ", a.nEntryNox" +
                            ", a.sOrderNox" +
                            ", a.sStockIDx" +
                            ", a.nQuantity" +
                            ", a.sNotesxxx" +
                            ", a.dModified" +
                            ", b.sBarCodex xBarCodex" +
                            ", b.sDescript xDescript" +
                            ", c.sDescript xCategr01" +
                            ", d.sDescript xCategr02" +
                            ", e.sDescript xInvTypNm" +
                            ", f.cClassify" +
                            ", f.nQtyOnHnd" +
                            ", f.nResvOrdr" +
                            ", f.nBackOrdr" +
                            ", f.nOnTranst" +
                            ", f.nAvgMonSl" +
                            ", f.sNotesxxx" +
                            ", f.nQuantity xQuantity" +
                            ", f.nIssueQty" +
                            ", f.nOrderQty" +
                            ", g.sDescript xBrandNme" +
                            ", h.sDescript xModelNme" +
                            ", h.sDescript xModelDsc" +
                            ", i.sDescript xColorNme" +
                            ", j.sMeasurNm xMeasurNm" +
                        " FROM " + System.getProperty("sys.table") + " a" + 
                            " LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx" +
                            " LEFT JOIN Category c ON b.sCategCd1 = c.sCategrCd" +
                            " LEFT JOIN Category_Level2 d ON b.sCategCd2 = d.sCategrCd" +
                            " LEFT JOIN Inv_Type e ON d.sInvTypCd = e.sInvTypCd" +
                            " LEFT JOIN Inv_Stock_Request_Detail f ON b.sStockIDx = f.sStockIDx" +
                            " LEFT JOIN Brand g ON b.sBrandIDx = g.sBrandIDx" +
                            " LEFT JOIN Model h ON b.sModelIDx = h.sModelIDx" +
                            " LEFT JOIN Color i ON b.sColorIDx = i.sColorIDx" +
                            " LEFT JOIN Measure j ON b.sMeasurID = j.sMeasurID"+ 
                        " WHERE 0=1";
                        
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), System.getProperty("sys.table"), "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
