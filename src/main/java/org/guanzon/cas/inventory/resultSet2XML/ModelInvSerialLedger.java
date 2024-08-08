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
public class ModelInvSerialLedger {
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
        System.setProperty("sys.table", "Model_Inv_Serial_Ledger");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECT" +
                            	"  a.sSerialID" +
                                ", a.sBranchCd" +
                                ", a.nLedgerNo" +
                                ", a.dTransact" +
                                ", a.sSourceCd" +
                                ", a.sSourceNo" +
                                ", a.cSoldStat" +
                                ", a.cLocation" +
                                ", a.dModified" +
                                ", c.sBarCodex xBarCodex" +
                                ", c.sDescript xDescript" +
                                ", b.sSerial01 xSerial01" +
                                ", b.sSerial02 xSerial02" +
                        " FROM Inv_Serial_Ledger a"+ 
                            " LEFT JOIN Inv_Serial b ON a.sSerialID = b.sSerialID" +
                            " LEFT JOIN Inventory c ON b.sStockIDx = c.sStockIDx";
        
        
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
