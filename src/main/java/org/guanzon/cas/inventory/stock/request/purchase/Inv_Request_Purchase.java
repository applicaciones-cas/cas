package org.guanzon.cas.inventory.stock.request.purchase;

import org.guanzon.cas.inventory.stock.request.issuance.*;
import org.guanzon.cas.inventory.stock.request.approval.*;
import org.guanzon.cas.inventory.stock.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.inventory.stock.request.RequestIssuanceController;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.guanzon.cas.inventory.stock.request.RequestPurchaseController;
import org.json.simple.JSONObject;

/**
 *
 * @author Unclejo
 */
public class Inv_Request_Purchase implements RequestPurchaseController  {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;
    RequestIssuanceController poTrans;
    RequestControllerFactory factory = new RequestControllerFactory();
        
    RequestControllerFactory.RequestType type; // Example type
    RequestControllerFactory.RequestCategoryType categ_type; // Example type

    private boolean p_bWithUI = true;
    JSONObject poJSON;

    /**
     *
     * @param types
     */
    @Override
    public void setType(RequestControllerFactory.RequestType types){
        type = types;
        poTrans = factory.makeIssuance(type, poGRider, p_bWithUI);
        poTrans.setType(type);
        
    }
    

    @Override
    public void setCategoryType(RequestControllerFactory.RequestCategoryType type) {
        categ_type = type;
        poTrans.setCategoryType(categ_type);
    }
    
    public void setWithUI(boolean fbValue){
        p_bWithUI = fbValue;

    }
    public Inv_Request_Purchase(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public int getItemCount() {
        return poTrans.getItemCount();
    }

    @Override
    public Model_Inv_Stock_Request_Detail getDetailModel(int fnRow) {
        return poTrans.getDetailModel(fnRow);
    }

    @Override
    public JSONObject setDetail(int fnRow, int fnCol, Object foData) {
        return poTrans.setDetail(fnRow, fnCol, foData);
    }

    @Override
    public JSONObject setDetail(int fnRow, String fsCol, Object foData) {
        return poTrans.setDetail(fnRow, fsCol, foData);
    }

    @Override
    public JSONObject searchDetail(int fnRow, String fsCol, String fsValue, boolean bln) {
        return poTrans.searchDetail(fnRow, fsCol, fsValue, bln);
    }

    @Override
    public JSONObject searchDetail(int fnRow, int fnCol, String fsValue, boolean bln) {
        return poTrans.searchDetail(fnRow, fnCol, fsValue, bln);
    }

    @Override
    public JSONObject newTransaction() {
        return poTrans.newTransaction();
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        return poTrans.openTransaction(fsValue);
    }

    @Override
    public JSONObject updateTransaction() {
        return poTrans.updateTransaction();
    }

    @Override
    public JSONObject saveTransaction() {
        return poTrans.saveTransaction();
    }

    @Override
    public JSONObject deleteTransaction(String fsValue) {
        return poTrans.deleteTransaction(fsValue);
    }

    @Override
    public JSONObject closeTransaction(String fsValue) {
        return poTrans.closeTransaction(fsValue);
    }

    @Override
    public JSONObject postTransaction(String fsValue) {
        return poTrans.postTransaction(fsValue);
    }

    @Override
    public JSONObject voidTransaction(String fsValue) {
        return poTrans.voidTransaction(fsValue);
    }

    @Override
    public JSONObject cancelTransaction(String fsValue) {
        return poTrans.cancelTransaction(fsValue);
    }

    @Override
    public JSONObject searchWithCondition(String fsValue) {
        return poTrans.searchWithCondition(fsValue);
    }

    @Override
    public JSONObject searchTransaction(String fsCol, String fsValue, boolean bln) {
        return poTrans.searchTransaction(fsCol, fsValue, bln);
    }

    @Override
    public JSONObject searchMaster(String fsCol, String fsValue, boolean bln) {
        return poTrans.searchMaster(fsCol, fsValue, bln);
    }

    @Override
    public JSONObject searchMaster(int fnCol, String fsValue, boolean bln) {
        return poTrans.searchMaster(fnCol, fsValue, bln);
    }

    @Override
    public Model_Inv_Stock_Request_Master getMasterModel() {
        return poTrans.getMasterModel();
    }

    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poTrans.setMaster(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsValue, Object foData) {
        return poTrans.setMaster(fsValue, foData);
    }

    @Override
    public int getEditMode() {
        return poTrans.getEditMode();
    }

    @Override
    public void setTransactionStatus(String fsValue) {
        poTrans.setTransactionStatus(fsValue);
    }

    @Override
    public ArrayList<Model_Inv_Stock_Request_Detail> getDetailModel() {
        return poTrans.getDetailModel();
    }

    @Override
    public void cancelUpdate() {
        poTrans.cancelUpdate();
    }
    
}
