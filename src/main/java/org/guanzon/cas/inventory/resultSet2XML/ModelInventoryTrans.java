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
public class ModelInventoryTrans {
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
        System.setProperty("sys.table", "Inventory_Trans");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECT" +
                            "  a.sStockIDx" +
                            ", a.sBranchCd" +
                            ", a.sWHouseID" +
                            ", a.nQtyOnHnd nQuantity" +
                            ", b.nQtyInxxx" +
                            ", b.nQtyOutxx" +
                            ", b.nQtyOrder" +
                            ", b.nQtyIssue" +
                            ", b.nQtyOnHnd" +
                            ", a.nBackOrdr" +
                            ", a.nResvOrdr" +
                            ", a.nFloatQty" +
                            ", a.nLedgerNo" +
                            ", a.dAcquired" +
                            ", a.dBegInvxx" +
                            ", a.dLastTran" +
                            ", b.nPurPrice" +
                            ", b.nUnitPrce" +
                            ", b.dExpiryxx" +
                            ", c.cUnitType" +
                            ", '0' cNewParts" +
                            ", '' sReplacID" +
                            ", c.cSerialze" +
                            ", a.cRecdStat" +
               
                        " FROM Inv_Master a"+ 
                            " LEFT JOIN Inv_Ledger b ON a.sStockIDx = b.sStockIDx AND a.sBranchCd = b.sBranchCd"+ 
                            " LEFT JOIN Inventory c ON a.sStockIDx = c.sStockIDx";
        
        
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
