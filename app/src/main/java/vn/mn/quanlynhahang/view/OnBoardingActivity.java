package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.relex.circleindicator.CircleIndicator;
import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.ViewPagerAdapter;

public class OnBoardingActivity extends AppCompatActivity {
    private TextView txtSkip;
    private ViewPager viewPager;
    private RelativeLayout layoutBottom;
    private CircleIndicator circleIndicator;
    private LinearLayout llNext;
    private FirebaseAuth mAuth;
    private ViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(OnBoardingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            setupOnBoarding();
        }

    }

    private void setupOnBoarding() {
        txtSkip = findViewById(R.id.txtSkip);
        viewPager = findViewById(R.id.viewpager);
        layoutBottom = findViewById(R.id.layout_bottom);
        circleIndicator = findViewById(R.id.circleindicator);
        llNext = findViewById(R.id.llNext);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mAdapter);
        circleIndicator.setViewPager(viewPager);

        txtSkip.setOnClickListener(v -> {
            viewPager.setCurrentItem(2);
        });

        llNext.setOnClickListener(v -> {
            if(viewPager.getCurrentItem() < 2){
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }else if(viewPager.getCurrentItem() == 2){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    txtSkip.setVisibility(View.GONE);
                    layoutBottom.setGravity(View.GONE);
                }else{
                    txtSkip.setVisibility(View.VISIBLE);
                    layoutBottom.setGravity(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}