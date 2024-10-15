package org.guanzon.cas.inventory.stock.request.purchase;

import org.guanzon.cas.inventory.stock.request.issuance.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.InventoryTrans;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory.RequestType;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory.RequestCategoryType;
import org.guanzon.cas.inventory.stock.request.RequestIssuanceController;
import org.guanzon.cas.inventory.stock.request.RequestPurchaseController;
import org.guanzon.cas.parameters.Category;
import org.guanzon.cas.parameters.Inv_Type;
import org.json.simple.JSONObject;

/**
 *
 * @author Unclejo
 */
public class Inv_Request_MC_Purchase implements RequestPurchaseController {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psTranStatus;

    private boolean p_bWithUI = true;
    Inv_Request poRequest;
    
    RequestType type;
    RequestCategoryType category_type;

    int roqSaveCount = 0;
    JSONObject poJSON;

    public void setWithUI(boolean fbValue) {
        p_bWithUI = fbValue;
    }

    public Inv_Request_MC_Purchase(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        poRequest = new Inv_Request(foGRider, fbWthParent);
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public void setType(RequestType types) {
        type = types;
        poRequest.setType(type);
    }

    @Override
    public void setCategoryType(RequestCategoryType type) {
        category_type = type;
        poRequest.setCategoryType(category_type);
    }

    @Override
    public int getItemCount() {
        return poRequest.getItemCount();
    }

    @Override
    public Model_Inv_Stock_Request_Detail getDetailModel(int fnRow) {
        return poRequest.getDetailModel(fnRow);
    }

    @Override
    public JSONObject setDetail(int fnRow, int fnCol, Object foData) {
        return poRequest.setDetail(fnRow, fnCol, foData);
    }

    @Override
    public JSONObject setDetail(int fnRow, String fsCol, Object foData) {
        return poRequest.setDetail(fnRow, fsCol, foData);
    }

    @Override
    public JSONObject searchDetail(int fnRow, String fsCol, String fsValue, boolean bln) {
        return poRequest.searchDetail(fnRow, fsCol, fsValue, bln);
    }

    @Override
    public JSONObject searchDetail(int fnRow, int fnCol, String fsValue, boolean bln) {
        return poRequest.searchDetail(fnRow, fnCol, fsValue, bln);
    }

    @Override
    public JSONObject newTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        return poRequest.openTransaction(fsValue);
    }

    @Override
    public JSONObject updateTransaction() {
        return poRequest.updateTransaction();
    }

    @Override
    public JSONObject saveTransaction() {
        return poRequest.saveTransaction();
    }

    @Override
    public JSONObject deleteTransaction(String fsValue) {
        return poRequest.deleteTransaction(fsValue);
    }

    @Override
    public JSONObject closeTransaction(String fsValue) {
        return poRequest.closeTransaction(fsValue);
    }

    @Override
    public JSONObject postTransaction(String fsValue) {
        return poRequest.postTransaction(fsValue);
    }

    @Override
    public JSONObject voidTransaction(String fsValue) {
        return poRequest.voidTransaction(fsValue);
    }

    @Override
    public JSONObject cancelTransaction(String fsValue) {
        return poRequest.cancelTransaction(fsValue);
    }

    @Override
    public JSONObject searchWithCondition(String fsValue) {
        return poRequest.searchWithCondition(fsValue);
    }

    @Override
    public JSONObject searchTransaction(String fsCol, String fsValue, boolean bln) {
        return poRequest.searchTransaction(fsCol, fsValue, bln);
    }

    @Override
    public JSONObject searchMaster(String fsCol, String fsValue, boolean bln) {
        return poRequest.searchMaster(fsCol, fsValue, bln);
    }

    @Override
    public JSONObject searchMaster(int fnCol, String fsValue, boolean bln) {
        return poRequest.searchMaster(fnCol, fsValue, bln);
    }

    @Override
    public Model_Inv_Stock_Request_Master getMasterModel() {
        return poRequest.getMasterModel();
    }

    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poRequest.setMaster(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsValue, Object foData) {
        return poRequest.setMaster(fsValue, foData);
    }

    @Override
    public int getEditMode() {
        return poRequest.getEditMode();
    }

    @Override
    public void setTransactionStatus(String fsValue) {
        poRequest.setTransactionStatus(fsValue);
    }

    @Override
    public ArrayList<Model_Inv_Stock_Request_Detail> getDetailModel() {
        return poRequest.getDetailModel();
    }



    @Override
    public void cancelUpdate() {
        poRequest.cancelUpdate();
    }

}
