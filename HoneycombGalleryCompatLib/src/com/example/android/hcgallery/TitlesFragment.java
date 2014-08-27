/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hcgallery;

import android.support.v4.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment {
    private int mCategory = 0;
    private int mCurPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Current position should survive screen rotations.
        if (savedInstanceState != null) {
            mCategory = savedInstanceState.getInt("category");
            mCurPosition = savedInstanceState.getInt("listPosition");
        }

        populateTitles(mCategory);
        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setCacheColorHint(Color.TRANSPARENT);
        selectPosition(mCurPosition);
    }


    public void populateTitles(int category) {
        DirectoryCategory cat = Directory.getCategory(category);
        String[] items = new String[cat.getEntryCount()];
        for (int i = 0; i < cat.getEntryCount(); i++)
            items[i] = cat.getEntry(i).getName();
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.title_list_item, items));
        mCategory = category;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        updateImage(position);
    }

    private void updateImage(int position) {
        ContentFragment frag = (ContentFragment) getFragmentManager()
                .findFragmentById(R.id.frag_content);
        frag.updateContentAndRecycleBitmap(mCategory, position);
        mCurPosition = position;
    }

    public void selectPosition(int position) {
        ListView lv = getListView();
        lv.setItemChecked(position, true);
        updateImage(position);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("listPosition", mCurPosition);
        outState.putInt("category", mCategory);
    }
}
