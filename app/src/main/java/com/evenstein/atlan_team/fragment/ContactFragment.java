package com.evenstein.atlan_team.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evenstein.atlan_team.R;

/**
 *    This Fragment shows a contact Information about author.
 *
 *  @author Sokolsky Vitaly
 *  @version 1.0
 *
 */

public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        return view;
    }

}
