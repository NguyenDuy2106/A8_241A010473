package vn.edu.vhu.ltdd.a8fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

/** Màn hình chi tiết tin, nhận dữ liệu qua arguments (Bundle). */
public class NewsDetailFragment extends Fragment {

    public static final String KEY_TIEU_DE = "key_tieu_de";
    private static final String KEY_NOI_DUNG = "key_noi_dung";

    /**
     * Mẫu newInstance: KHÔNG tạo constructor có tham số cho Fragment.
     * Khi hệ thống khôi phục Fragment sau khi xoay máy, nó chỉ gọi constructor rỗng;
     * dữ liệu chỉ sống sót nếu nằm trong arguments.
     */
    public static NewsDetailFragment newInstance(String tieuDe, String noiDung) {
        NewsDetailFragment f = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TIEU_DE, tieuDe);
        args.putString(KEY_NOI_DUNG, noiDung);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String tieuDe = (args != null) ? args.getString(KEY_TIEU_DE, "") : "";
        String noiDung = (args != null) ? args.getString(KEY_NOI_DUNG, "") : "";

        ((TextView) view.findViewById(R.id.tvTieuDe)).setText(tieuDe);
        ((TextView) view.findViewById(R.id.tvNoiDung)).setText(noiDung);

        Button btnDaDoc = view.findViewById(R.id.btnDaDoc);
        btnDaDoc.setOnClickListener(v -> {
            // NC2: Cập nhật trạng thái bài tin đã đọc vào SharedViewModel
            SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            sharedViewModel.setLastReadNews(tieuDe);

            // Gửi kết quả về Fragment
            Bundle kq = new Bundle();
            kq.putString(KEY_TIEU_DE, tieuDe);
            getParentFragmentManager().setFragmentResult(NewsListFragment.REQUEST_KEY, kq);

            // Nếu không phải là giao diện Tablet (không có detailContainer) thì quay về màn hình trước
            View detailContainer = requireActivity().findViewById(R.id.detailContainer);
            if (detailContainer == null) {
                try {
                    NavHostFragment.findNavController(this).navigateUp();
                } catch (Exception e) {
                    getParentFragmentManager().popBackStack();   // tương đương bấm nút Back
                }
            }
        });
    }
}
