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
public class ModelInvSerial {
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
        System.setProperty("sys.table", "Inv_Serial");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECT " +
                "     a.sSerialID " +
                "   , a.sBranchCd " +
                "   , a.sSerial01 " +
                "   , a.sSerial02 " +
                "   , a.nUnitPrce " +
                "   , a.sStockIDx " +
                "   , a.cLocation " +
                "   , a.cSoldStat " +
                "   , a.cUnitType " +
                "   , a.sCompnyID " +
                "   , a.sWarranty " +
                "   , a.dModified " +
                "   , b.sBarCodex xBarCodex " +
                "   , b.sDescript xDescript " +
                "   , c.sDescript xBrandNme " +
                "   , d.sModelNme xModelNme " +
                "   , e.sDescript xColorNme " +
                "   , IFNULL(f.sMeasurNm,'') xMeasurNm " +
                "   , IFNULL(g.sDescript,'') xCategrNm " +
                "   , h.sBranchNm xBranchNm  " +
                "   , i.sCompnyNm xCompanyNm  " +
                "  FROM " + System.getProperty("sys.table") + " a " +
                "      LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx " +
                "      LEFT JOIN Brand c ON b.sBrandCde = c.sBrandCde " +
                "      LEFT JOIN Model d ON b.sModelCde = d.sModelCde " +
                "      LEFT JOIN Color e ON b.sColorCde = e.sColorCde " +
                "      LEFT JOIN Measure f ON b.sMeasurID = f.sMeasurID " +
                "      LEFT JOIN Category g ON b.sCategCd1 = g.sCategrCd"+
                "      LEFT JOIN Branch h ON a.sBranchCd = h.sBranchCd " +
                "      LEFT JOIN Company i ON a.sCompnyID = i.sCompnyID " ;
        
        
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
