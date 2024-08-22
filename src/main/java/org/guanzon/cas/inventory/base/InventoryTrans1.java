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
import org.guanzon.cas.inventory.models.Model_Inv_Ledger;
import org.guanzon.cas.inventory.models.Model_Inv_Master;
import org.guanzon.cas.inventory.models.Model_Inventory_Trans;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class InventoryTrans1 implements GTransaction{
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
    
    private ArrayList<Model_Inv_Master> poInvMaster;
    private ArrayList<Model_Inv_Ledger> poModelLedger;
    private ArrayList<Model_Inv_Master> poInvMasterProcessd;
    private ArrayList<Model_Inv_Ledger> popoModelLedgerProcessd;
    
    private InvMaster poMaster;
    private InvLedger poLedger;
    private InvLedger poLedgerProcess;
    private ResultSet poRSDetail;
    
    private final String pxeLastTran = "2018-10-01";
    private final String pxeModuleName = "InventoryTrans";
    
    public InventoryTrans1(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        
        psSourceCd = "";
        psSourceNo = "";
        pnEditMode = EditMode.UNKNOWN;
        poLedger = new InvLedger(poGRider, pbWthParent);
        
    }
    @Override
    public JSONObject newTransaction() {
        poJSON = new JSONObject();
        try{

            Connection loConn = null;
            loConn = setConnection();
            
            poInvMaster = new ArrayList<>();
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
           
//        if (!processInventory()) return false;
//        
//        return saveDetail();
return poJSON;
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
    
    public ArrayList<Model_Inv_Master> getMaster(){return poInvMaster;}
    public void setMaster(ArrayList<Model_Inv_Master> foObj){this.poInvMaster = foObj;}
    
    public ArrayList<Model_Inv_Ledger> getLedger(){return poModelLedger;}
    public void setLedger(ArrayList<Model_Inv_Ledger> foObj){this.poModelLedger = foObj;}


    public void setMaster(int fnRow, int fnIndex, Object foValue){ poInvMaster.get(fnRow).setValue(fnIndex, foValue);}
    public void setMaster(int fnRow, String fsIndex, Object foValue){ poInvMaster.get(fnRow).setValue(fsIndex, foValue);}
    public Object getMaster(int fnRow, int fnIndex){return poInvMaster.get(fnRow).getValue(fnIndex);}
    public Object getMaster(int fnRow, String fsIndex){return poInvMaster.get(fnRow).getValue(fsIndex);}
    
    
    public void seLedger(int fnRow, int fnIndex, Object foValue){ poLedger.setMaster(fnRow, fnIndex, foValue);}
    public void seLedger(int fnRow, String fsIndex, Object foValue){ poLedger.setMaster(fnRow, fsIndex, foValue);}
    public Object geLedger(int fnRow, int fnIndex){return poLedger.getMaster(fnRow, fnIndex);}
    public Object geLedger(int fnRow, String fsIndex){return poLedger.getMaster(fnRow, fsIndex);}

    public JSONObject addDetail(){
        poJSON = new JSONObject();
        if (poInvMaster.isEmpty()){
            poInvMaster.add(new Model_Inv_Master(poGRider));
            poInvMaster.get(0).newRecord();
            
            poJSON = poLedger.addLedger();
            poJSON.put("result", "success");
            poJSON.put("message", "Inventory add record.");
            

        } else {
            poInvMaster.add(new Model_Inv_Master(poGRider));
            poInvMaster.get(poInvMaster.size()-1).newRecord();
            
            poJSON = poLedger.addLedger();
            
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
    
    private int findOnProcInventory(String fsIndex, Object fsValue){
        if (popoModelLedgerProcessd.isEmpty()) return -1;
        
        for (int lnCtr=0; lnCtr <= popoModelLedgerProcessd.size()-1; lnCtr++){
            if (popoModelLedgerProcessd.get(lnCtr).getValue(fsIndex).equals(fsValue)) 
                return lnCtr;
        }
        return -1;
    }
}
