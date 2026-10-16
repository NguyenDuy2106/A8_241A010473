package vn.edu.vhu.ltdd.a8fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/** NC2: SharedViewModel lưu trữ và giữ trạng thái dùng chung giữa các Fragment qua ViewModelProvider(requireActivity()) */
public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> lastReadNews = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>("Nguyễn Duy");
    private final MutableLiveData<String> userMssv = new MutableLiveData<>("241A010473");

    public LiveData<String> getLastReadNews() {
        return lastReadNews;
    }

    public void setLastReadNews(String newsTitle) {
        lastReadNews.setValue(newsTitle);
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName.setValue(name);
    }

    public LiveData<String> getUserMssv() {
        return userMssv;
    }

    public void setUserMssv(String mssv) {
        userMssv.setValue(mssv);
    }
}
