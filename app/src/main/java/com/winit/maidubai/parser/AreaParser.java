package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 02-09-2016.
 */
public class AreaParser extends BaseJsonHandler {

    private HashMap<Integer,ArrayList<AreaDO>> hmAreaDOs;

    public Object getData()
    {
        return status==0?message:hmAreaDOs;
    }
    public void parse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if (status != 0 && jsonObject.has("AreaandSubAreas")) {
                JSONArray jsonAreaArray = jsonObject.getJSONArray("AreaandSubAreas");
                int jsonAreaArrayLength = jsonAreaArray.length();
                hmAreaDOs = new HashMap<>();
                for(int i=0;i<jsonAreaArrayLength;i++){
                    JSONObject jsonArea = jsonAreaArray.getJSONObject(i);
                    AreaDO areaDO = new AreaDO();
                    areaDO.AreaId = jsonArea.getInt("AreaId");
                    areaDO.ParentId = jsonArea.getInt("ParentId");
                    areaDO.Name = jsonArea.getString("Name");
                    areaDO.CreatedDate = jsonArea.getString("CreatedDate");
                    areaDO.CreatedBy = jsonArea.getString("CreatedBy");
                    areaDO.ModifiedDate = jsonArea.getString("ModifiedDate");
                    areaDO.ModifiedBy = jsonArea.getString("ModifiedBy");
                    ArrayList<AreaDO> arrAreaDOs = hmAreaDOs.get(areaDO.ParentId);
                    if(arrAreaDOs==null)
                        arrAreaDOs = new ArrayList<>();
                    arrAreaDOs.add(areaDO);
                    hmAreaDOs.put(areaDO.ParentId,arrAreaDOs);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }
}
