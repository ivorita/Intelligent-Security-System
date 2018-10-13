package com.antelope.android.bottomnavigation.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.bottomnavigation.R;
import com.antelope.android.bottomnavigation.data.DS;
import com.antelope.android.bottomnavigation.data.IntegerData;
import com.antelope.android.bottomnavigation.data.Picture;
import com.antelope.android.bottomnavigation.net.APIService;
import com.antelope.android.bottomnavigation.utils.DownloadUtils;
import com.antelope.android.bottomnavigation.utils.GlideApp;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public class DataFragment extends Fragment implements OnRefreshListener {

    @BindView(R.id.temp)
    TextView mTemp;
    @BindView(R.id.hum)
    TextView mHum;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.update_time)
    TextView mUpdateTime;
    @BindView(R.id.update_time_1)
    TextView mUpdateTime1;
    @BindView(R.id.state_door)
    TextView mStateDoor;
    @BindView(R.id.update_time_2)
    TextView mUpdateTime2;
    @BindView(R.id.pic)
    ImageView mPic;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.update_time_3)
    TextView mUpdateTime3;
    @BindView(R.id.temp_min)
    TextView mTempMin;
    @BindView(R.id.temp_max)
    TextView mTempMax;
    @BindView(R.id.hum_min)
    TextView mHumMin;
    @BindView(R.id.hum_max)
    TextView mHumMax;
    @BindView(R.id.fire_state)
    TextView mFireState;
    @BindView(R.id.update_time4)
    TextView mUpdateTime4;
    private CompositeDisposable mSubscriptions;
    private DownloadUtils mDownloadUtils;
    private Unbinder unbinder;

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSubscriptions = new CompositeDisposable();
        mDownloadUtils = new DownloadUtils();
        mSrl.setOnRefreshListener(this);
        getTemp();
        getHum();
        getDstate();
        fetchPic();
        getTdTempMin();
        getTdTempMax();
        getTdHumMin();
        getTdHumMax();
        getFireState();
        return view;
    }

    public void getTemp() {
        Disposable subscribe = APIService.getInstance().apis.lastestTemp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData tp = ds.getData();
                        int tmp = tp.getCurrent_value();
                        String temp = getString(R.string.temp, tmp);
                        String date = tp.getUpdate_at();
                        mTemp.setText(temp);
                        mUpdateTime.setText(date);
                        System.out.println(tmp);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getHum() {
        Disposable subscribe = APIService.getInstance().apis.lastestHum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData hm = ds.getData();
                        int hmy = hm.getCurrent_value();
                        String humy = getString(R.string.humidity, hmy);
                        String date = hm.getUpdate_at();
                        mHum.setText(humy);
                        mUpdateTime1.setText(date);
                        System.out.println("hmy" + hmy);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getDstate() {
        Disposable subscribe = APIService.getInstance().apis.lastestDState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData dst = ds.getData();
                        int dste = dst.getCurrent_value();
                        System.out.println("状态：" + dste);
                        if (dste == 0) {
                            mStateDoor.setText("开");
                        } else if (dste == 1) {
                            mStateDoor.setText("关");
                        }
                        String date = dst.getUpdate_at();
                        mUpdateTime2.setText(date);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void loadPic(String index) {

        System.out.println("mDownloadUtils: " + mDownloadUtils.downloadImg(index));

        mDownloadUtils.downloadImg(index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Glide.with(getActivity()).load(bitmap).into(mPic);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        /*Toast.makeText(getApplicationContext(), "接收完成", Toast.LENGTH_SHORT).show();*/
                    }
                });
    }

    public void fetchPic() {
        final Disposable subscribe = APIService.getInstance().apis.latestPic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<Picture>>() {
                    @Override
                    public void accept(DS<Picture> ds) throws Exception {
                        Picture picture = ds.getData();
                        String index = picture.getCurrent_value().getIndex();
                        System.out.println("index: " + index);
                        String date = picture.getUpdate_at();
                        mUpdateTime3.setText(date);
                        loadPic(index);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getTdTempMin() {
        Disposable subscribe = APIService.getInstance().apis.latest("Temp_Min")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData TM = ds.getData();
                        int tmn = TM.getCurrent_value();
                        String TMN = getString(R.string.temp, tmn);
                        String date = TM.getUpdate_at();
                        mTempMin.setText(TMN);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getTdTempMax() {
        Disposable subscribe = APIService.getInstance().apis.latest("Temp_Max")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData TM = ds.getData();
                        int tmx = TM.getCurrent_value();
                        String TMX = getString(R.string.temp, tmx);
                        String date = TM.getUpdate_at();
                        mTempMax.setText(TMX);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getTdHumMin() {
        Disposable subscribe = APIService.getInstance().apis.latest("Hum_Min")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData TM = ds.getData();
                        int hmn = TM.getCurrent_value();
                        String HMN = getString(R.string.humidity, hmn);
                        String date = TM.getUpdate_at();
                        mHumMin.setText(HMN);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getTdHumMax() {
        Disposable subscribe = APIService.getInstance().apis.latest("Hum_Max")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData TM = ds.getData();
                        int hmx = TM.getCurrent_value();
                        String HMX = getString(R.string.humidity, hmx);
                        String date = TM.getUpdate_at();
                        mHumMax.setText(HMX);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public void getFireState() {
        Disposable subscribe = APIService.getInstance().apis.latest("Fire")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DS<IntegerData>>() {
                    @Override
                    public void accept(DS<IntegerData> ds) throws Exception {
                        IntegerData fs = ds.getData();
                        int fire = fs.getCurrent_value();

                        if (fire == 0) {
                            mFireState.setText("无");
                        } else if (fire == 1) {
                            mFireState.setText("有");
                        }
                        String date = fs.getUpdate_at();
                        mUpdateTime4.setText(date);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mSubscriptions.clear();
    }

    @Override
    public void onRefresh() {
        getTemp();
        getHum();
        getDstate();
        fetchPic();
        getTdTempMin();
        getTdTempMax();
        getTdHumMin();
        getTdHumMax();
        getFireState();
        mSrl.setRefreshing(false);
    }
}
