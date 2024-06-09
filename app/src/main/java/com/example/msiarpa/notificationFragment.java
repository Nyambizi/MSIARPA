package com.example.msiarpa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class notificationFragment extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        viewPager2 = view.findViewById(R.id.viewPager);
        viewPager2.setAdapter(new Notification_Adapter(this));

        tabLayout = view.findViewById(R.id.tableLayout);

        TabLayoutMediator tabLayoutMediator =  new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("new notification");
                        tab.setIcon(R.drawable.new_notification);
                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.mocublue)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(100);
                        break;
                    }
                    case 1:{
                        tab.setText("new supervisor");
                        tab.setIcon(R.drawable.cycle_24px);
                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.mocublue)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(60);
                        break;
                    }
                    case 2:{
                        tab.setText("new upload");
                        tab.setIcon(R.drawable.progress);
                        BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.mocublue)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(100);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }

                }

            }
        }
        );

        tabLayoutMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable= tabLayout.getTabAt(position).getBadge();
                badgeDrawable.setVisible(false);
            }
        });

        return view;
    }
}