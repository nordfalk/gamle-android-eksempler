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

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ContentFragment extends Fragment {
    private View mContentView;

    // The bitmap currently used by ImageView
    private Bitmap mBitmap = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.content_welcome, null);
        mContentView.setDrawingCacheEnabled(false);

        return mContentView;
    }

    void updateContentAndRecycleBitmap(int category, int position) {

        if (mBitmap != null) {
            // This is an advanced call and should be used if you
            // are working with a lot of bitmaps. The bitmap is dead
            // after this call.
            mBitmap.recycle();
        }

        // Get the bitmap that needs to be drawn and update the ImageView
        mBitmap = Directory.getCategory(category).getEntry(position)
                .getBitmap(getResources());
        ((ImageView) getView().findViewById(R.id.image)).setImageBitmap(mBitmap);
    }

}
