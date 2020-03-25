package com.peteleco.appet.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peteleco.appet.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {


    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        TextView textContato = view.findViewById(R.id.textContatos);
        textContato.setText("Contatos Alterados");


        return view;
    }

}
