package vn.edu.vhu.ltdd.a8fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

/** Danh sách tin. Bấm một tin → mở NewsDetailFragment và đẩy vào back stack. */
public class NewsListFragment extends Fragment {

    private static final String TAG = "A8_241A010473/List";

    /** Khóa của "kênh" trao đổi kết quả giữa hai Fragment. */
    public static final String REQUEST_KEY = "news_request";

    private static final String[] TIEU_DE = {
            "VHU khai giảng năm học mới",
            "Android Studio ra bản cập nhật",
            "Flutter 3.47 có gì mới?",
    };
    private static final String[] NOI_DUNG = {
            "Trường Đại học Văn Hiến tổ chức lễ khai giảng với sự tham dự của toàn thể sinh viên khóa mới…",
            "Bản cập nhật tập trung vào tốc độ build, trình soạn thảo layout và công cụ phân tích bộ nhớ…",
            "Phiên bản này bổ sung RadioGroup, cải tiến hiệu năng trên web và nhiều widget Material 3…",
    };

    private TextView tvDaDoc;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDaDoc = view.findViewById(R.id.tvDaDoc);
        LinearLayout box = view.findViewById(R.id.boxTin);

        // NC2: Sử dụng ViewModelProvider(requireActivity()) để giữ trạng thái dùng chung giữa các tab
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getLastReadNews().observe(getViewLifecycleOwner(), tieuDe -> {
            if (tieuDe != null && !tieuDe.isEmpty()) {
                tvDaDoc.setText(getString(R.string.list_last_read, tieuDe));
            }
        });

        // Tạm thời sinh nút bằng code; từ Lab A9 sẽ thay bằng RecyclerView + Adapter
        for (int i = 0; i < TIEU_DE.length; i++) {
            final int viTri = i;
            Button b = new Button(requireContext());
            b.setText(TIEU_DE[i]);
            b.setAllCaps(false);
            b.setOnClickListener(v -> moChiTiet(viTri));
            box.addView(b);
        }

        // Lắng nghe kết quả do NewsDetailFragment gửi về (khi chạy không dùng ViewModel)
        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY, getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    String tieuDe = bundle.getString(NewsDetailFragment.KEY_TIEU_DE, "");
                    Log.d(TAG, "Nhận kết quả: đã đọc " + tieuDe);
                    sharedViewModel.setLastReadNews(tieuDe);
                });
    }

    private void moChiTiet(int viTri) {
        // NC1: Kiểm tra xem có khung detailContainer (bố cục Master-Detail cho Tablet) hay không
        View detailContainer = requireActivity().findViewById(R.id.detailContainer);
        if (detailContainer != null) {
            // Đang ở màn hình Tablet (Master-Detail): Nạp trực tiếp Fragment chi tiết vào cột bên phải
            NewsDetailFragment f = NewsDetailFragment.newInstance(TIEU_DE[viTri], NOI_DUNG[viTri]);
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.detailContainer, f)
                    .commit();
        } else {
            // Màn hình điện thoại đơn lẻ: Chuyển trang qua Navigation Component
            Bundle args = new Bundle();
            args.putString(NewsDetailFragment.KEY_TIEU_DE, TIEU_DE[viTri]);
            args.putString("key_noi_dung", NOI_DUNG[viTri]);

            try {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_newsList_to_newsDetail, args);
            } catch (Exception e) {
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.nav_host_fragment, NewsDetailFragment.newInstance(TIEU_DE[viTri], NOI_DUNG[viTri]))
                        .addToBackStack("chi_tiet")
                        .setReorderingAllowed(true)
                        .commit();
            }
        }
    }
}
