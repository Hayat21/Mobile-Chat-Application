package com.example.messagerie.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.messagerie.Fragment.Amis_fragment;
import com.example.messagerie.Fragment.Message_fragment;
import com.example.messagerie.Fragment.Utilisateur_fragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Utilisateur_fragment usersFragment=new Utilisateur_fragment();
                    return usersFragment;

                case 1:
                    Message_fragment chatsFragment=new Message_fragment();
                    return chatsFragment;
                case 2:
                   Amis_fragment friendsFragment=new Amis_fragment();
                    return friendsFragment;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Utilisateur";
                case 1:
                    return "Discussion";
                case 2:
                    return "Amis";
                default:
                    return null;

            }
        }
    }

