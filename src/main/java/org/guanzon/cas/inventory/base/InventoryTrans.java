/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inventory.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.cas.inventory.constant.InvConstants;
import org.guanzon.cas.model.inventory.Model_Inv_Master;
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    
    
}
