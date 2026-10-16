package vn.edu.vhu.ltdd.a8fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class HomeFragment extends Fragment {
    private static final String TAG = "A8_241A010473/Home";
    @Override
    public void onAttach (@NonNull android.content.Context context){
        super.onAttach(context);
        Log.d(TAG, "onCreate – tạo Fragment (chưa có View)");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle saveedInstanceState){
        Log.d(TAG, " onCreateView - tạo cây View");
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        Log.d(TAG, "onCreated - anh xa View");
        TextView tv=view.findViewById(R.id.tvChao);
        tv.setText(getString(R.string.home_hello,"Nguyen Duy"));
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG,"onDestroyView - View bị hủy (Fragment có thể vẫn còn sống)");
    }
}
