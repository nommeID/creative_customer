package com.nomme.creative.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nomme.creative.DTO.AllAtristListDTO;
import com.nomme.creative.DTO.CategoryDTO;
import com.nomme.creative.DTO.UserDTO;
import com.nomme.creative.R;
import com.nomme.creative.https.HttpsRequest;
import com.nomme.creative.interfacess.Consts;
import com.nomme.creative.interfacess.Helper;
import com.nomme.creative.interfacess.OnSpinerItemClick;
import com.nomme.creative.network.NetworkManager;
import com.nomme.creative.preferences.SharedPrefrence;
import com.nomme.creative.ui.activity.BaseActivity;
import com.nomme.creative.ui.adapter.DiscoverAdapter;
import com.nomme.creative.utils.CustomTextViewBold;
import com.nomme.creative.utils.ProjectUtils;
import com.nomme.creative.utils.SpinnerDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class DiscoverFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String TAG = DiscoverFragment.class.getSimpleName();
    private View view;
    private RecyclerView rvDiscover;
    private DiscoverAdapter discoverAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    HashMap<String, String> parms = new HashMap<>();
    private LayoutInflater myInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomTextViewBold tvFilter, tvNotFound;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private SpinnerDialog spinnerDialogCate;
    AlertDialog alertDialog1;
    CharSequence[] values = {" Price(Low to High) ", " Jobs Done ", " Featured ", " Favourite "};
    private ArrayList<AllAtristListDTO> tempList;
    private BaseActivity baseActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        myInflater = LayoutInflater.from(getActivity());
        getCategory();
        setUiAction();
        return view;

    }

    public void setUiAction() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        tvNotFound = view.findViewById(R.id.tvNotFound);
        tvFilter = view.findViewById(R.id.tvFilter);
        baseActivity.ivFilter.setOnClickListener(this);
        tvFilter.setOnClickListener(this);

        rvDiscover = view.findViewById(R.id.rvDiscover);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvDiscover.setLayoutManager(mLayoutManager);

        discoverAdapter = new DiscoverAdapter(getActivity(), allAtristListDTOList, myInflater);
        rvDiscover.setAdapter(discoverAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            parms.put(Consts.CATEGORY_ID, "");
                                            getArtist();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFilter:
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.no_cate_found));
                }
                break;
            case R.id.ivFilter:
                CreateAlertDialogWithRadioButtonGroup();
                break;

        }
    }


    public void getArtist() {
        parms.put(Consts.LATITUDE, prefrence.getValue(Consts.LATITUDE));
        parms.put(Consts.LONGITUDE, prefrence.getValue(Consts.LONGITUDE));
        // ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_ARTISTS_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                //ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        allAtristListDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AllAtristListDTO>>() {
                        }.getType();
                        allAtristListDTOList = (ArrayList<AllAtristListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    tvNotFound.setVisibility(View.VISIBLE);
                    rvDiscover.setVisibility(View.GONE);
                    baseActivity.ivFilter.setVisibility(View.GONE);
                }
            }
        });
    }


    public void showData() {
        tvNotFound.setVisibility(View.GONE);
        rvDiscover.setVisibility(View.VISIBLE);
        baseActivity.ivFilter.setVisibility(View.VISIBLE);
        discoverAdapter = new DiscoverAdapter(getActivity(), allAtristListDTOList, myInflater);
        rvDiscover.setAdapter(discoverAdapter);
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        parms.put(Consts.CATEGORY_ID, "");
        tvFilter.setText(getResources().getString(R.string.all_category));
        getArtist();
    }

    public void getCategory() {


        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        spinnerDialogCate = new SpinnerDialog((Activity) getActivity(), categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvFilter.setText(item);
                                parms.put(Consts.CATEGORY_ID, id);
                                getArtist();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public void shortlistlowtohigh() {
        Collections.sort(allAtristListDTOList, new Comparator<AllAtristListDTO>() {

            public int compare(AllAtristListDTO obj1, AllAtristListDTO obj2) {
                int az = Integer.parseInt(obj1.getPrice());
                int za = Integer.parseInt(obj2.getPrice());
                return (az < za) ? -1 : (az > za) ? 1 : 0;

            }
        });
    }

    public void shortJobs() {
        Collections.sort(allAtristListDTOList, new Comparator<AllAtristListDTO>() {

            public int compare(AllAtristListDTO obj1, AllAtristListDTO obj2) {
                int az = Integer.parseInt(obj1.getJobDone());
                int za = Integer.parseInt(obj2.getJobDone());
                return (az > za) ? -1 : (az > za) ? 1 : 0;

            }
        });
    }

    public void shortFeatured() {
        Collections.sort(allAtristListDTOList, new Comparator<AllAtristListDTO>() {

            public int compare(AllAtristListDTO obj1, AllAtristListDTO obj2) {
                int az = Integer.parseInt(obj1.getFeatured());
                int za = Integer.parseInt(obj2.getFeatured());
                return (az > za) ? -1 : (az > za) ? 1 : 0;

            }
        });
    }

    public void shortFavourite() {
        Collections.sort(allAtristListDTOList, new Comparator<AllAtristListDTO>() {

            public int compare(AllAtristListDTO obj1, AllAtristListDTO obj2) {
                int az = Integer.parseInt(obj1.getFav_status());
                int za = Integer.parseInt(obj2.getFav_status());

                return (az > za) ? -1 : (az > za) ? 1 : 0;

            }
        });
    }


    public void CreateAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Sort by");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        shortlistlowtohigh();
                        discoverAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        shortJobs();
                        discoverAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        shortFeatured();
                        discoverAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        shortFavourite();
                        discoverAdapter.notifyDataSetChanged();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }


}
