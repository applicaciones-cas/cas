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
public class ModelInvHistLedger {
    public static void main (String [] args){
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        System.setProperty("sys.default.path.config", path);

        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        System.setProperty("sys.table", "Inv_Hist_Ledger");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECTF" +
                        "   a.sStockIDx" +
                        " , a.sBranchCd" +
                        " , a.sWHouseID" +
                        " , a.nLedgerNo" +
                        " , a.dTransact" +
                        " , a.sSourceCd" +
                        " , a.sSourceNo" +
                        " , a.nQtyInxxx" +
                        " , a.nQtyOutxx" +
                        " , a.nQtyOrder" +
                        " , a.nQtyIssue" +
                        " , a.nPurPrice" +
                        " , a.nUnitPrce" +
                        " , a.nQtyOnHnd" +
                        " , a.dExpiryxx" +
                        " , a.sModified" +
                        " , a.dModified" +
                        " , b.sBarCodex xBarCodex" +
                        " , b.sDescript xDescript" +
                        " , c.sWHouseNm xWHouseNm " +
                        "FROM " + System.getProperty("sys.table") + " a" +
                        "    LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx" +
                        "    LEFT JOIN Warehouse c ON a.sWhouseID = c.sWhouseID";
        
        
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
