package com.example.msiarpa;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Notification_Adapter extends FragmentStateAdapter {


    public Notification_Adapter(@NonNull notificationFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new  Notification_New_Fragment();
            case 1:
                return new  Notification_uploads_Fragment();

            default:
                return new Notification_supervisor_Fragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
