package com.example.coolweather.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coolweather.R;
import com.example.coolweather.WeatherActivity;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0 ;
    public static final int LEVEL_CITY = 1 ;
    public static final int LEVEL_COUNTY = 2 ;
    private ProgressDialog progressDialog ;
    private TextView titleText ;
    private Button btn_back ;
    private ListView listView ;
    private ArrayAdapter<String> adapter ;
    private List<String> dataList = new ArrayList<>() ;
    private List<Province> provinceList ;
    private List<City> cityList ;
    private List<County> countyList ;
    private Province selectedProvince ;
    private City selectedCity ;
    private County selectedCounty ;
    private int currentLevel ;
    
    private final String TAG = "TAG" ; 
    // 获取控件实例，初始化ArrayAdapter
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false) ;
        titleText = (TextView) view.findViewById(R.id.titleText) ;
        btn_back = (Button) view.findViewById(R.id.back_button) ;
        listView = (ListView)view.findViewById(R.id.list_view) ;
        dataList.add("test") ;
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList) ;
        listView.setAdapter(adapter);
        return view ;
    }

    //
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel ==  LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position) ;
                    queryCities();
                }else if(currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId() ;
                    Intent intent = new Intent(getActivity(), WeatherActivity.class) ;
                    intent.putExtra("weatherId",weatherId) ;
                    startActivity(intent);
                    getActivity().finish() ;
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText("中国");
        btn_back.setVisibility(View.GONE);
        // 从数据库获取所有省份数据
        provinceList = DataSupport.findAll(Province.class) ;
        Log.d("TAG", "queryProvinces: province len="+provinceList.size());
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName()) ;
            }
            Log.d(TAG, "queryProvinces: dataList len = "+dataList.size());
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE ;
        }else{
            String address = "http://guolin.tech/api/china" ;
            queryFromServer(address,"province") ;
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class) ;
        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName()) ;
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY ;
        }else{
            int provinecCode = selectedProvince.getProvinceCode() ;
            String address = "http://guolin.tech/api/china/" + provinecCode ;
            queryFromServer(address,"city");
        }
    }

    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        btn_back.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",
                String.valueOf(selectedCity.getId())).find(County.class) ;
        if(countyList.size()>0){
            dataList.clear();
            Log.d(TAG, "queryCounties: datalist = "+dataList.size());
            for(County county : countyList){
                dataList.add(county.getCountryName()) ;
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY ;
        }else{
            int provinceCode = selectedProvince.getProvinceCode() ;
            int cityCode = selectedCity.getCityCode() ;
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode ;
            queryFromServer(address,"county");
        }

    }
    /*
    *   根据传入地址address和类型（province、city、county）从服务器上查询省市县数据
    * */
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        if(isNetworkAvailable(getContext())){
            Log.d(TAG, "queryFromServer: network is available");
        }else{
            Log.d(TAG, "queryFromServer: network is no available");
        }
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: type = "+type);
                String responseText = response.body().string() ;
                Log.d(TAG, "onResponse: response = "+responseText);
                boolean result = false ;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText) ;
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId()) ;
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId()) ;
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            } else if("conuty".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread回到主线程处理
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败....",Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
    /*************   显示进度对话框    **************/
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(getActivity()) ;
            progressDialog.setMessage("正在加载.........");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /*************   关闭进度对话框    **************/
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    /**************     有无网络连接       *************/
    public boolean isNetworkAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) ;
        if(manager == null)
            return false ;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo() ;
        if(networkInfo==null || !networkInfo.isAvailable())
            return false ;
        return true ;
    }
}
