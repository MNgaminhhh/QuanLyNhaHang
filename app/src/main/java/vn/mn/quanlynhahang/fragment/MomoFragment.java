package vn.mn.quanlynhahang.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vn.mn.quanlynhahang.R;

public class MomoFragment extends Fragment {
    ImageView imgMomo;
    Button btnBack;
    int price;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_momo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgMomo = view.findViewById(R.id.imgMomo);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        Bundle args = getArguments();
        if (args != null) {
            price = args.getInt("total", 0);}
        try {
            imgMomo.setImageBitmap(generateQRCodeBitmap(price));
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
    public static Bitmap generateQRCodeBitmap(int price) throws WriterException {
        BitMatrix bitMatrix;
        String barcodeText = "2|99|0366114003|Nguyen Van Thi||0|0|"+price;
        try {
            bitMatrix = new QRCodeWriter().encode(barcodeText, BarcodeFormat.QR_CODE, 250, 250);
        } catch (WriterException e) {
            e.printStackTrace();
            throw e;
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}