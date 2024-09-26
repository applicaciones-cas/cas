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
public class ModelInvMaster {
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
        System.setProperty("sys.table", "Inv_Master");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECT" +
                            "   a.sStockIDx" +
                            " , a.sBranchCd" +
                            " , a.sWHouseID" +
                            " , a.sLocatnID" +
                            " , a.nBinNumbr" +
                            " , a.dAcquired" +
                            " , a.dBegInvxx" +
                            " , a.nBegQtyxx" +
                            " , a.nQtyOnHnd" +
                            " , a.nLedgerNo" +
                            " , a.nMinLevel" +
                            " , a.nMaxLevel" +
                            " , a.nAvgMonSl" +
                            " , a.nAvgCostx" +
                            " , a.cClassify" +
                            " , a.nBackOrdr" +
                            " , a.nResvOrdr" +
                            " , a.nFloatQty" +
                            " , a.dLastTran" +
                            " , a.cPrimaryx" +
                            " , a.cRecdStat" +
                            " , a.sModified" +
                            " , a.dModified" +
                            " , b.sBarCodex xBarCodex" +
                            " , b.sDescript xDescript" +
                            " , c.sWHouseNm xWHouseNm" +
                            " , d.sDescript xLocatnNm" +
                            " , e.sSectnNme xSectnNme" +
                        " FROM Inv_Master a"+ 
                            " LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx" +
                            " LEFT JOIN Warehouse c ON a.sWhouseID = c.sWhouseID" +
                            " LEFT JOIN Inv_Location d ON a.sLocatnID = d.sLocatnID" +
                            " LEFT JOIN Section e ON e.sSectnIDx = d.sSectnIDx";

        
        
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
