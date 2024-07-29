/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.cas.inventory.constant.InvConstants;
import org.guanzon.cas.model.inventory.Model_Inv_Master;
import org.guanzon.cas.model.inventory.Model_Inventory_Sub_Unit;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class InventoryTrans implements GTransaction{
    GRider poGRider;
    boolean pbWthParent;
    String psBranchCd;
    boolean pbWtParent;
    public JSONObject poJSON;
    
    String psTranStatus;
    
    private boolean pbWarehous;
    private boolean pbInitTran;
    
    private String psSourceCd;
    private String psSourceNo;
    private String psClientID;
    private Date pdTransact;
    private int pnEditMode;
    
    private String psWarnMsg = "";
    private String psErrMsgx = "";
    
    private ArrayList<Model_Inv_Master> poModel;
    private ArrayList<Model_Inv_Master> poModelProcessd;
    private ResultSet poRSDetail;
    
    private final String pxeLastTran = "2018-10-01";
    private final String pxeModuleName = "InventoryTrans";
    
    public InventoryTrans(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        
        psSourceCd = "";
        psSourceNo = "";
        pnEditMode = EditMode.UNKNOWN;
        
    }
    @Override
    public JSONObject newTransaction() {
        poJSON = new JSONObject();
        try{

            Connection loConn = null;
            loConn = setConnection();
            
            poModel = new ArrayList<>();
            poJSON = addDetail();
            if (poJSON.get("result").equals("error")){
                
                poJSON.put("result", "error");
                poJSON.put("message", "initialized new record failed.");
                return poJSON;
            }else{
                poJSON.put("result", "success");
                poJSON.put("message", "initialized new record.");
                pnEditMode = EditMode.ADDNEW;
            }

        }catch(NullPointerException e){

            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }

        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject updateTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject saveTransaction() {
        poJSON = new JSONObject();
        //check update mode
        if (!(pnEditMode == EditMode.ADDNEW ||
                pnEditMode == EditMode.DELETE)){
            poJSON.put("result","error");
            poJSON.put("message", "Invalid Update Mode Detected.");
            return poJSON;
        }
        
        if (!loadTransaction()){
            poJSON.put("result","error");
            poJSON.put("message", "Unable to load transaction.");
            return poJSON;
        } 
        
        if (pnEditMode == EditMode.DELETE){
            return delDetail();
        }
           
        if (!processInventory()) return false;
        
        return saveDetail();
    }

    @Override
    public JSONObject deleteTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject closeTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject postTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject voidTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject cancelTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchWithCondition(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchTransaction(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchMaster(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject searchMaster(int i, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object getMasterModel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject setMaster(int i, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject setMaster(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }

    private Connection setConnection(){
        Connection foConn;

        if (pbWthParent){
            foConn = (Connection) poGRider.getConnection();
            if (foConn == null) foConn = (Connection) poGRider.doConnect();
        }else foConn = (Connection) poGRider.doConnect();

        return foConn;
    }

    private JSONObject checkData(JSONObject joValue){
        if(pnEditMode == EditMode.READY ||
                pnEditMode == EditMode.ADDNEW ||
                    pnEditMode == EditMode.UPDATE){
            if(joValue.containsKey("continue")){
                if(true == (boolean)joValue.get("continue")){
                    joValue.put("result", "success");
                    joValue.put("message", "Record saved successfully.");
                }
            }
        }
        return joValue;
    }

    @Override
    public void setTransactionStatus(String fsValue) {
        psTranStatus = fsValue;
    }

    
    public JSONObject AcceptDelivery(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.ACCEPT_DELIVERY;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject Accept2HDelivery(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.ACCEPT_2H_DELIVERY;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject Delivery2H(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.ACCEPT_2H_DELIVERY;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject BranchTransfer(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.BRANCH_TRANSFER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject AutoAcceptDelivery(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.AUTO_ACCEPT_DELIVERY;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject AutoDelivery(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.AUTO_DELIVERY;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject JobOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.JOB_ORDER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject Purchase(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.PURCHASE;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject PurchaseReceiving(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.PURCHASE_RECEIVING;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject PurchaseReturn(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.PURCHASE_RETURN;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject PurchaseReplacement(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.PURCHASE_REPLACEMENT;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject Wholesale(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.WHOLESALE;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject WholesaleReturn(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.WHOLESALE_RETURN;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject WholesalesReplacement(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.WHOLESALE_REPLACAMENT;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject Sales(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.SALES;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject SalesReturn(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.SALES_RETURN;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject SalesReplacement(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.SALES_REPLACEMENT;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject SalesGiveaway(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.SALES_GIVE_AWAY;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject WarrantyRelease(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.WARRANTY_RELEASE;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject BranchOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.BRANCH_ORDER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject BranchOrderConfirm(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.BRANCH_ORDER_CONFIRM;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject CustomerOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.CUSTOMER_ORDER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject RetailOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.RETAIL_ORDER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject CancelRetailOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.CANCEL_RETAIL_ORDER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject WholesaleOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.WHOLESALE_ORDER;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject WholesaleCancelOrder(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.WHOLESALE_ORDER_CANCEL;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject Salvage_Cannibalized(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.SALVAGE_CANNIBALIZED;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject CreditMemo(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.CREDIT_MEMO;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject DebitMemo(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.DEBIT_MEMO;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public JSONObject GCardRedeem(String fsSourceNo,
                                    Date fdTransDate,
                                    int fnUpdateMode){
        psSourceCd = InvConstants.GCARD_REDEEM;
        psSourceNo = fsSourceNo;
        pdTransact = fdTransDate;
        pnEditMode = fnUpdateMode;
    
        return saveTransaction();
    }
    
    public ArrayList<Model_Inv_Master> getMaster(){return poModel;}
    public void setMaster(ArrayList<Model_Inv_Master> foObj){this.poModel = foObj;}


    public void setMaster(int fnRow, int fnIndex, Object foValue){ poModel.get(fnRow).setValue(fnIndex, foValue);}
    public void setMaster(int fnRow, String fsIndex, Object foValue){ poModel.get(fnRow).setValue(fsIndex, foValue);}
    public Object getMaster(int fnRow, int fnIndex){return poModel.get(fnRow).getValue(fnIndex);}
    public Object getMaster(int fnRow, String fsIndex){return poModel.get(fnRow).getValue(fsIndex);}

    public JSONObject addDetail(){
        poJSON = new JSONObject();
        if (poModel.isEmpty()){
            poModel.add(new Model_Inv_Master(poGRider));
            poModel.get(0).newRecord();
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory add record.");
            

        } else {
            poModel.add(new Model_Inv_Master(poGRider));
            poModel.get(poModel.size()-1).newRecord();
            
            poJSON.put("result", "success");
            poJSON.put("message", "Address add record.");
        }
        return poJSON;
    }
    private JSONObject delDetail(){
       String lsMasSQL;
        String lsLgrSQL;
        poJSON =  new JSONObject();
        
        try {
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRSDetail); lnCtr++){
                poRSDetail.absolute(lnCtr);
                lsMasSQL = "UPDATE Inv_Master SET" +
                                "  nQtyOnHnd = nQtyOnHnd + " + (poRSDetail.getInt("nQtyOutxx") -  poRSDetail.getInt("nQtyInxxx")) +
                                ", nBackOrdr = nBackOrdr - " + poRSDetail.getInt("nQtyOrder") +
                                ", nResvOrdr = nResvOrdr + " + poRSDetail.getInt("nQtyIssue") +
                                ", nLedgerNo = " + (poRSDetail.getInt("nLedgerNo") - 1) +
                                ", dModified = " + SQLUtil.toSQL(poGRider.getServerDate()) +
                            " WHERE sStockIDx = " + SQLUtil.toSQL(poRSDetail.getString("sStockIDx")) +
                                " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd);
                
                lsLgrSQL = "DELETE FROM Inv_Ledger" +
                            " WHERE sStockIDx = " + SQLUtil.toSQL(poRSDetail.getString("sStockIDx")) +
                               " AND sSourceCd = " + SQLUtil.toSQL(psSourceCd) +
                               " AND sSourceNo = " + SQLUtil.toSQL(psSourceNo) + 
                                " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd);
                
                if (poGRider.executeQuery(lsMasSQL, "Inv_Master", psBranchCd, "") <= 0){
                    poJSON.put("result", "error");
                    poJSON.put("message", poGRider.getErrMsg() + "\n" + poGRider.getMessage());
                    return poJSON;
                }
                
                if (poGRider.executeQuery(lsLgrSQL, "Inv_Ledger", psBranchCd, "") <= 0){
                    poJSON.put("result", "error");
                    poJSON.put("message", poGRider.getErrMsg() + "\n" + poGRider.getMessage());
                    return poJSON;
                }
                
                poJSON.put("result", "success");
                poJSON.put("message", "Record saved successfuly.");
                
                //TODO: re align on hand
            }
        } catch (SQLException ex) {
            
            poJSON.put("result", "error");
            poJSON.put("message", "Please inform MIS Deparment." + "\n" + ex.getMessage());
            return poJSON;
        }

        return poJSON;
    }
    
    private boolean loadTransaction(){
        String lsSQL = "SELECT" + 
                            "  a.sStockIDx" + 
                            ", a.nLedgerNo" +
                            ", a.dTransact" +
                            ", a.nQtyInxxx" + 
                            ", a.nQtyOutxx" + 
                            ", a.nQtyOrder" + 
                            ", a.nQtyIssue" + 
                            ", a.nQtyOnHnd" + 
                            ", b.nBackOrdr" + 
                            ", b.nResvOrdr" + 
                            ", b.nLedgerNo xLedgerNo" + 
                        " FROM Inv_Ledger a" +  
                            ", Inv_Master b" + 
                        " WHERE a.sStockIDx = b.sStockIDx" + 
                            " AND a.sBranchCd = b.sBranchCd";
                    
        if (pnEditMode == EditMode.ADDNEW){
            lsSQL = lsSQL + " AND 0=1";
        } else {
            lsSQL = lsSQL + 
                        " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCd) + 
                        " AND a.sSourceCd = " + SQLUtil.toSQL(psSourceCd) +
                        " AND a.sSourceNo = " + SQLUtil.toSQL(psSourceNo) + 
                    " ORDER BY a.sStockIDx";
        }
        
        poRSDetail = poGRider.executeQuery(lsSQL);
        return true;
    }
    private JSONObject processInventory(){
        String lsSQL;
        ResultSet loRS;
        int lnRow;
        
        poModelProcessd = new ArrayList<>();
        poJSON = new JSONObject();
       
        for (int lnCtr = 0; lnCtr <= poModel.size()-1; lnCtr++){
            lnRow = findOnProcInventory("sStockIDx", poModel.get(lnCtr).getStockID());
            
            //-1 if no record found on filter
            if (lnRow == -1){
                lsSQL = "SELECT" +
                            "  a.nQtyOnHnd" + 
                            ", a.nBackOrdr" + 
                            ", a.nResvOrdr" + 
                            ", a.nFloatQty" + 
                            ", a.nLedgerNo" + 
                            ", a.dBegInvxx" + 
                            ", a.dAcquired" + 
                            ", a.nBegQtyxx" + 
                            ", a.cRecdStat" + 
                            ", c.nLedgerNo xLedgerNo" + 
                            ", IFNULL(c.nQtyOnHnd, 0) xQtyOnHnd" +
                            ", b.dTransact dLastTran" +
                            ", a.sStockIDx" +
                        " FROM Inv_Master a" +
                            " LEFT JOIN Inv_Ledger b" + 
                                " ON a.sBranchCd = b.sBranchCd" +
                                    " AND a.sStockIDx = b.sStockIDx" +
                                    " AND a.nLedgerNo = b.nLedgerNo" +
                            " LEFT JOIN Inv_Ledger c" +
                                " ON a.sBranchCd = c.sBranchCd" +
                                    " AND a.sStockIDx = c.sStockIDx" +
                                    " AND c.dTransact <= " + SQLUtil.toSQL(pdTransact) + 
                        " WHERE a.sStockIDx = " + SQLUtil.toSQL(poModel.get(lnCtr).getStockID()) + 
                            " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                        " ORDER BY c.dTransact DESC" +
                            ", c.nLedgerNo DESC" +
                        " LIMIT 1";
                
                loRS = poGRider.executeQuery(lsSQL);
                
                poModelProcessd.add(new Model_Inv_Master(poGRider));
                lnRow = poModelProcessd.size()-1;
                
                if (MiscUtil.RecordCount(loRS) == 0){
//                    poModelProcessd.get(lnRow).IsNewParts("1");
                    poModelProcessd.get(lnRow).setQtyOnHnd(0);
                    poModelProcessd.get(lnRow).setLedgerNo(0);
                    poModelProcessd.get(lnRow).setBackOrdr(0);
                    poModelProcessd.get(lnRow).setResvOrdr(0);
                    poModelProcessd.get(lnRow).setFloatQty(0);
                    poModelProcessd.get(lnRow).setLastTranDate(pdTransact.toString());
//                    poModelProcessd.get(lnRow).setDateExpire(pdTransact);

                    poModelProcessd.get(lnRow).setRecdStat(RecordStatus.ACTIVE);
                } else {
                    try {
                        loRS.first();
//                        poModelProcessd.get(lnRow).IsNewParts("0");
                        poModelProcessd.get(lnRow).setQtyOnHnd(loRS.getDouble("nQtyOnHnd"));
                        poModelProcessd.get(lnRow).setLedgerNo(loRS.getInt("nLedgerNo"));
                        poModelProcessd.get(lnRow).setBackOrdr(loRS.getDouble("nBackOrdr"));
                        poModelProcessd.get(lnRow).setResvOrdr(loRS.getDouble("nResvOrdr"));
                        poModelProcessd.get(lnRow).setFloatQty(loRS.getDouble("nFloatQty"));
                        poModelProcessd.get(lnRow).setRecdStat(loRS.getString("cRecdStat"));
                        
                        if (loRS.getDate("dAcquired") != null) poModelProcessd.get(lnRow).setAcquiredDate(loRS.getDate("dAcquired"));
                        if (loRS.getDate("dLastTran") != null){
                            long diffInMillies = Math.abs(pdTransact.getTime() - loRS.getDate("dLastTran").getTime());
                            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            
                            if (diffInDays > 0){
                                if (loRS.getInt("xLedgerNo") == 0){
                                    poModelProcessd.get(lnRow).setLedgerNo(1);
                                    poModelProcessd.get(lnRow).setQtyOnHnd(loRS.getDouble("nBegQtyxx"));
                                }else {
                                    poModelProcessd.get(lnRow).setLedgerNo(loRS.getInt("xLedgerNo"));
                                    poModelProcessd.get(lnRow).setQtyOnHnd(loRS.getDouble("xQtyOnHnd"));
                                }
                                poModelProcessd.get(lnRow).setLastTranDate(loRS.getDate("dLastTran").toString());
                            }else{
                                poModelProcessd.get(lnRow).setLastTranDate(new SimpleDateFormat("yyyy-MM-dd").parse(pxeLastTran).toString());
                            }
                        }
                    } catch (SQLException | ParseException e) {
                        poJSON.put("result", "error");
                        poJSON.put("message", e.getMessage());
                        return poJSON;
                    } 
                }
                
                poModelProcessd.get(lnRow).setStockID(poModel.get(lnCtr).getStockID());
                poModelProcessd.get(lnRow).setQtyInxxx(0);
                poModelProcessd.get(lnRow).setQtyOutxx(0);
                poModelProcessd.get(lnRow).setQtyIssue(0);
                poModelProcessd.get(lnRow).setQtyOrder(0);
            }
            
            switch (psSourceCd){
                case InvConstants.ACCEPT_DELIVERY:
//                    poModelProcessd.get(lnRow).setQtyInxxx( poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
//                                                        +  poModel.get(lnCtr).getQuantity().doubleValue());
//                    if (pbWarehous){
//                        if (poModel.get(lnCtr).getReplacID().equals("")){
//                            poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
//                                                                - poModel.get(lnCtr).getQuantity().doubleValue());
//                        }
//                    } 
                    
                    poModelProcessd.get(lnRow).setQtyInxxx( poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        +  poModel.get(lnCtr).getQuantity().doubleValue());//alway based on order
                    if (pbWarehous){
                        if (poModel.get(lnCtr).getReplacID().equals("")){
                            poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                                - poModel.get(lnCtr).getQuantity().doubleValue());
                        }
                    } 
                    
                    
                    break;
                    case InvConstants.ACCEPT_DELIVERY_DISCREPANCY:
                        
                        System.err.println("qtyin" +poModelProcessd.get(lnRow).getQtyInxxx());
                        System.err.println("qtyinput?" +poModelProcessd.get(lnRow).getQuantity());
                    poModelProcessd.get(lnRow).setQtyInxxx( poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        +  poModel.get(lnCtr).getQuantity().doubleValue());
                    if (pbWarehous){
                        if (poModel.get(lnCtr).getReplacID().equals("")){
                            poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                                - poModel.get(lnCtr).getQuantity().doubleValue());
                        }
                    } 
                    break;
                case InvConstants.BRANCH_ORDER:
                    poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.BRANCH_ORDER_CONFIRM:
                case InvConstants.CUSTOMER_ORDER:
                case InvConstants.RETAIL_ORDER:
                    poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                        - poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.CANCEL_RETAIL_ORDER:
                case InvConstants.WHOLESALE_ORDER_CANCEL:
                    poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.DELIVERY:
//                    poModelProcessd.get(lnRow).setDateExpire(poModel.get(lnCtr).getDateExpire());
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    
                    if (poModel.get(lnCtr).getReplacID().equals("")){
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + poModel.get(lnCtr).getQuantity().doubleValue());
                    }
                    break;
                    case InvConstants.DELIVERY_DISCREPANCY:
//                    poModelProcessd.get(lnRow).setDateExpire(poModel.get(lnCtr).getDateExpire());
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    
                    if (poModel.get(lnCtr).getReplacID().equals("")){
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + poModel.get(lnCtr).getQuantity().doubleValue());
                    }
                    break;
                case InvConstants.JOB_ORDER:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    
                    if (poModel.get(lnCtr).getReplacID().equals("")){
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + poModel.get(lnCtr).getResvOrdr().doubleValue());
                    }        
                    break;
                case InvConstants.PURCHASE:
                    poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                        + poModel.get(lnCtr).getQtyOrder().doubleValue());
                    poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                        + poModel.get(lnCtr).getQtyIssue().doubleValue());
                    break;
                case InvConstants.PURCHASE_RECEIVING:
                    poModelProcessd.get(lnRow).setQtyInxxx(poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
//                    poModelProcessd.get(lnRow).setDateExpire(poModel.get(lnCtr).getDateExpire());
                    poModelProcessd.get(lnRow).setPurchase(poModel.get(lnCtr).getPurchase());

                    if (poModel.get(lnCtr).getReplacID().equals("")){
                        poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                            - poModel.get(lnCtr).getQuantity().doubleValue());
                    }
                    break;
                case InvConstants.PURCHASE_RETURN:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.PURCHASE_REPLACEMENT:
                    poModelProcessd.get(lnRow).setQtyInxxx(poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.WHOLESALE:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.WHOLESALE_RETURN:
                    poModelProcessd.get(lnRow).setQtyInxxx(poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.WHOLESALE_REPLACAMENT:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.SALES:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                   
                    /*if (!poModel.get(lnCtr).getReplacID().equals("")){
                        poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx()
                                                            + poModel.get(lnCtr).getQuantity());
                    }*/
                    break;
                case InvConstants.SALES_RETURN:
                    poModelProcessd.get(lnRow).setQtyInxxx(poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.SALES_REPLACEMENT:
                case InvConstants.SALES_GIVE_AWAY:
                case InvConstants.WARRANTY_RELEASE:
                case InvConstants.DEBIT_MEMO:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModel.get(lnCtr).getQuantity().doubleValue());
//                    poModelProcessd.get(lnRow).setDateExpire(pdTransact);
                    break;
                case InvConstants.WASTE_INV:
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.CREDIT_MEMO:
                    poModelProcessd.get(lnRow).setQtyInxxx(poModel.get(lnCtr).getQuantity().doubleValue());
//                    poModelProcessd.get(lnRow).setDateExpire(pdTransact);
                    break;
                case InvConstants.DAILY_PRODUCTION_IN:
                    poModelProcessd.get(lnRow).setDateExpire(poModel.get(lnCtr).getDateExpire());
                    poModelProcessd.get(lnRow).setQtyInxxx(poModelProcessd.get(lnRow).getQtyInxxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    break;
                case InvConstants.DAILY_PRODUCTION_OUT:
                    poModelProcessd.get(lnRow).setDateExpire(pdTransact);
                    poModelProcessd.get(lnRow).setQtyOutxx(poModelProcessd.get(lnRow).getQtyOutxx().doubleValue()
                                                        + poModel.get(lnCtr).getQuantity().doubleValue());
                    
                    if (poModel.get(lnCtr).getReplacID().equals("")){
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + poModel.get(lnCtr).getQuantity().doubleValue());
                    }
                    break;
            }
            
            if (!poModel.get(lnCtr).getReplacID().equals("")){
                lnRow = findOnProcInventory("sStockIDx", poModel.get(lnCtr).getReplacID());
                
                if (lnRow == -1){
                    lsSQL = "SELECT" +
                                "  a.nQtyOnHnd" + 
                                ", a.nBackOrdr" + 
                                ", a.nResvOrdr" + 
                                ", a.nFloatQty" + 
                                ", a.nLedgerNo" + 
                                ", a.dBegInvxx" + 
                                ", a.dAcquired" + 
                                ", a.nBegQtyxx" + 
                                ", a.cRecdStat" + 
                                ", IFNULL(c.nQtyOnHnd, 0) xQtyOnHnd" +
                                ", b.dTransact dLastTran" +
                            " FROM Inv_Master a" +
                                " LEFT JOIN Inv_Ledger b" + 
                                    " ON a.sBranchCd = b.sBranchCd" +
                                        " AND a.sStockIDx = b.sStockIDx" +
                                        " AND a.nLedgerNo = b.nLedgerNo" +
                                " LEFT JOIN Inv_Ledger c" +
                                    " ON a.sBranchCd = c.sBranchCd" +
                                        " AND a.sStockIDx = c.sStockIDx" +
                                        " AND c.dTransact <= " + SQLUtil.toSQL(pdTransact) + 
                            " WHERE a.sStockIDx = " + SQLUtil.toSQL(poModel.get(lnCtr).getReplacID()) + 
                                " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                            " ORDER BY c.dTransact DESC" +
                                ", c.nLedgerNo DESC" +
                            " LIMIT 1";

                    loRS = poGRider.executeQuery(lsSQL);

                    poModelProcessd.add(new UnitInventoryTrans());
                    lnRow = poModelProcessd.size()-1;
                    
                    if (MiscUtil.RecordCount(loRS) == 0){
//                        poModelProcessd.get(lnRow).IsNewParts("1");
                        poModelProcessd.get(lnRow).setQtyOnHnd(0);
                        poModelProcessd.get(lnRow).setLedgerNo(0);
                        poModelProcessd.get(lnRow).setBackOrdr(0);
                        poModelProcessd.get(lnRow).setResvOrdr(0);
                        poModelProcessd.get(lnRow).setFloatQty(0);
                        poModelProcessd.get(lnRow).setLastTranDate(pdTransact);
                        poModelProcessd.get(lnRow).setRecdStat(RecordStatus.ACTIVE);
                    } else {
                        try {
                            loRS.first();
//                            poModelProcessd.get(lnRow).IsNewParts("0");
                            poModelProcessd.get(lnRow).setQtyOnHnd(loRS.getDouble("nQtyOnHnd"));
                            poModelProcessd.get(lnRow).setLedgerNo(loRS.getInt("nLedgerNo"));
                            poModelProcessd.get(lnRow).setBackOrdr(loRS.getDouble("nBackOrdr"));
                            poModelProcessd.get(lnRow).setResvOrdr(loRS.getDouble("nResvOrdr"));
                            poModelProcessd.get(lnRow).setFloatQty(loRS.getDouble("nFloatQty"));
                            poModelProcessd.get(lnRow).setRecdStat(loRS.getString("cRecdStat"));

                            if (loRS.getDate("dAcquired") != null) poModelProcessd.get(lnRow).setAcquiredDate(loRS.getDate("dAcquired"));
                            if (loRS.getDate("dLastTran") != null){
                                long diffInMillies = Math.abs(pdTransact.getTime() - loRS.getDate("dLastTran").getTime());
                                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                                if (diffInDays > 0){
                                    if (loRS.getInt("nLedgerNo") == 0){
                                        poModelProcessd.get(lnRow).setLedgerNo(1);
                                        poModelProcessd.get(lnRow).setQtyOnHnd(loRS.getDouble("nBegQtyxx"));
                                    }else {
                                        poModelProcessd.get(lnRow).setLedgerNo(loRS.getInt("nLedgerNo"));
                                        poModelProcessd.get(lnRow).setQtyOnHnd(loRS.getDouble("xQtyOnHnd"));
                                    }
                                    poModelProcessd.get(lnRow).setLastTranDate(loRS.getDate("dLastTran").toString());
                                }else{
                                    poModelProcessd.get(lnRow).setLastTranDate(new SimpleDateFormat("yyyy-MM-dd").parse(pxeLastTran).toString());
                                }
                            }
                        } catch (SQLException | ParseException e) {
                            poJSON.put("result", "error");
                            poJSON.put("message", e.getMessage());
                            return poJSON;
                        } 
                    }

                    poModelProcessd.get(lnRow).setStockIDx(poModel.get(lnCtr).getReplacID());
                    poModelProcessd.get(lnRow).setQtyInxxx(0);
                    poModelProcessd.get(lnRow).setQtyOutxx(0);
                    poModelProcessd.get(lnRow).setQtyIssue(0);
                    poModelProcessd.get(lnRow).setQtyOrder(0);
                }
                
                switch (psSourceCd){
                    case InvConstants.ACCEPT_DELIVERY_DISCREPANCY:
                    case InvConstants.ACCEPT_DELIVERY:
                        if (!pbWarehous){
                            poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                                - poModel.get(lnCtr).getQuantity().doubleValue());
                        }
                        break;
                    case InvConstants.DELIVERY:
                    case InvConstants.DELIVERY_DISCREPANCY:
                        
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + poModel.get(lnCtr).getQuantity().doubleValue());
                        break;
                    case InvConstants.JOB_ORDER:
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + poModel.get(lnCtr).getQuantity().doubleValue());
                        break;
                    case InvConstants.PURCHASE_RECEIVING:
                        poModelProcessd.get(lnRow).setQtyOrder(poModelProcessd.get(lnRow).getQtyOrder().doubleValue()
                                                            - poModel.get(lnCtr).getQuantity().doubleValue());
                        break;
                    case InvConstants.SALES:
                        poModelProcessd.get(lnRow).setQtyIssue(poModelProcessd.get(lnRow).getQtyIssue().doubleValue()
                                                            + Double.parseDouble(poModel.get(lnCtr).getResvOrdr().toString()));
                        break;
                }
            }
        }
        return true;
    }
    
    private int findOnProcInventory(String fsIndex, Object fsValue){
        if (poModelProcessd.isEmpty()) return -1;
        
        for (int lnCtr=0; lnCtr <= poModelProcessd.size()-1; lnCtr++){
            if (poModelProcessd.get(lnCtr).getValue(fsIndex).equals(fsValue)) 
                return lnCtr;
        }
        return -1;
    }
    
}
