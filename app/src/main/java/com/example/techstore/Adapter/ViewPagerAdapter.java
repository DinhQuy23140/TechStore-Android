package com.example.techstore.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.techstore.fragment.CancelOrdersFragment;
import com.example.techstore.fragment.CompletedFragment;
import com.example.techstore.fragment.OngoingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new OngoingFragment();
        else if (position == 1)
            return new CompletedFragment();
        else {
            return new CancelOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
