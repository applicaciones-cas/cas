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
public class ModelInventory {
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
        System.setProperty("sys.table", "Inventory");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_" + System.getProperty("sys.table") + ".xml");
//        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory.xml");
        
        
        String lsSQL = "SELECT" +
                            "  a.sStockIDx" +
                            ", a.sBarCodex" +
                            ", a.sDescript" +
                            ", a.sBriefDsc" +
                            ", a.sAltBarCd" +
                            ", a.sCategCd1" +
                            ", a.sCategCd2" +
                            ", a.sCategCd3" +
                            ", a.sCategCd4" +
                            ", a.sBrandIDx" +
                            ", a.sModelIDx" +
                            ", a.sColorIDx" +
                            ", a.sMeasurID" +
                            ", a.sInvTypCd" +
                            ", a.nUnitPrce" +
                            ", a.nSelPrice" +
                            ", a.nDiscLev1" +
                            ", a.nDiscLev2" +
                            ", a.nDiscLev3" +
                            ", a.nDealrDsc" +
                            ", a.nMinLevel" +
                            ", a.nMaxLevel" +
                            ", a.cComboInv" +
                            ", a.cWthPromo" +
                            ", a.cSerialze" +
                            ", a.cUnitType" +
                            ", a.cInvStatx" +
                            ", a.nShlfLife" +
                            ", a.sSupersed" +
                            ", a.cRecdStat" +
                            ", a.sModified" +
                            ", a.dModified" +
                            ", b.sDescript xCategNm1" +
                            ", c.sDescript xCategNm2" +
                            ", d.sDescript xCategNm3" +
                            ", e.sDescript xCategNm4" +
                            ", f.sDescript xBrandNme" +
                            ", g.sDescript xModelNme" +
                            ", g.sDescript xModelDsc" +
                            ", h.sDescript xColorNme" +
                            ", i.sMeasurNm xMeasurNm" +
                            ", j.sDescript xInvTypNm" +
                            ", k.sBarCodex xSuperCde" +
                            ", k.sDescript xSuperDsc" +
                            ", c.sMainCatx xMainCatx" +
                        " FROM Inventory a"+ 
                            " LEFT JOIN Category b ON a.sCategCd1 = b.sCategrCd" +
                            " LEFT JOIN Category_Level2 c ON a.sCategCd2 = c.sCategrCd" +
                            " LEFT JOIN Category_Level3 d ON a.sCategCd3 = d.sCategrCd" +
                            " LEFT JOIN Category_Level4 e ON a.sCategCd4 = e.sCategrCd" +
                            " LEFT JOIN Brand f ON a.sBrandIDx = f.sBrandIDx" +
                            " LEFT JOIN Model g ON a.sModelIDx = g.sModelIDx" +
                            " LEFT JOIN Color h ON a.sColorIDx = h.sColorIDx" +
                            " LEFT JOIN Measure i ON a.sMeasurID = i.sMeasurID" +
                            " LEFT JOIN Inv_Type j ON c.sInvTypCd = j.sInvTypCd" +
                            " LEFT JOIN Inventory k ON a.sSupersed = k.sStockIDx" ;
        
        
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
