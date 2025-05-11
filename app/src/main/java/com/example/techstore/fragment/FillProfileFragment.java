package com.example.techstore.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.PersonViewModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FillProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FillProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserRepository userRepository;
    PersonViewModel personViewModel;
    TextView tvDoB;
    EditText edtUsername, edtEmail, edtPhoneNumber, edtDOB;
    ImageView ivImg;
    Button btnUpdate;
    RelativeLayout rlEditImg;
    String encodeImg;

    public FillProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FillProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FillProfileFragment newInstance(String param1, String param2) {
        FillProfileFragment fragment = new FillProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            ivImg.setImageBitmap(bitmap);
                        } else {
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_default_user);
                            ivImg.setImageResource(R.drawable.background_default_user);
                        }
                        encodeImg = enCodeImage(bitmap);
                        //signUpViewModel.selectImage(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fill_profile, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userRepository = new UserRepository(requireContext());
        personViewModel = new PersonViewModel(userRepository);
        edtUsername = view.findViewById(R.id.edt_username);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhoneNumber = view.findViewById(R.id.edt_phone_number);
        tvDoB = view.findViewById(R.id.tv_dob);
        ivImg = view.findViewById(R.id.iv_img);
        rlEditImg = view.findViewById(R.id.rl_edit_img);

        personViewModel.loadUser();
        personViewModel.getImgUser().observe(getViewLifecycleOwner(), img -> {
            if (img != null) {
                byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivImg.setImageBitmap(decodedByte);
                encodeImg = img;
            } else {
                ivImg.setImageResource(R.drawable.background_default_user);
            }
        });
        personViewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            edtUsername.setText(username );
        });
        personViewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            edtEmail.setText(email);
        });
        personViewModel.getPhone().observe(getViewLifecycleOwner(), phoneNumber -> {
            edtPhoneNumber.setText(phoneNumber);
        });
        personViewModel.getDob().observe(getViewLifecycleOwner(), dob -> {
            tvDoB.setText(getString(R.string.person_dob) + dob);
        });

        rlEditImg.setOnClickListener(editImg -> {
            Intent choose = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            choose.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            launcher.launch(choose);
        });

        tvDoB.setOnClickListener(selectDate -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("DefaultLocale") DatePickerDialog slBirthDate = new DatePickerDialog(
                    requireContext(),
                    (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                        tvDoB.setText(String.format("%d/%d/%d", selectedDay, selectedMonth + 1, selectedYear));
                    },
                    year,
                    month,
                    day
            );
            slBirthDate.show();

        });

        btnUpdate = view.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(update -> {
            String username = edtUsername.getText().toString();
            String email = edtEmail.getText().toString();
            String phoneNumber = edtPhoneNumber.getText().toString();
            String dob = tvDoB.getText().toString();
            Map<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_USERNAME, username);
            user.put(Constants.KEY_EMAIL, email);
            user.put(Constants.KEY_PHONE, phoneNumber);
            user.put(Constants.KEY_DOB, dob);
            user.put(Constants.KEY_IMG, encodeImg);
            personViewModel.updateUser(user);
        });

        personViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private String enCodeImage(Bitmap bitmap){
        //set with
        int previewWith = 150;
        //set height
        int previewHeight = bitmap.getHeight() * previewWith / bitmap.getWidth();
        //scale image
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}