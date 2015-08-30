/*
 * Copyright (C) 2007 The Android Open Source Project
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
package lekt04_lister2;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class BenytExpandableListActivity extends ExpandableListActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Set up our adapter
    ExpandableListAdapter mAdapter = new MyExpandableListAdapter();
    setListAdapter(mAdapter);
    registerForContextMenu(getExpandableListView());
  }

  public TextView lavTextView() {
    TextView textView = new TextView(this);

    // Layout parameters for the ExpandableListView
    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

    textView.setLayoutParams(lp);
    // Center the text vertically
    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    // Set the text starting position
    textView.setPadding(36, 0, 0, 0);
    return textView;
  }

  private String[] groups = {"People Names", "Dog Names", "Cat Names", "Fish Names"};
  private String[][] children = {
          {"Arnold", "Barry", "Chuck", "David"},
          {"Ace", "Bandit", "Cha-Cha", "Deuce"},
          {"Fluffy", "Snuggles"},
          {"Goldy", "Bubbles"}
  };

  /**
   * A simple adapter which maintains an ArrayList of photo resource Ids.
   * Each photo is displayed as an image. This adapter supports clearing the
   * list of photos and adding a new photo.
   */
  public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    // Sample data set.  children[i] contains the children (String[]) for groups[i].

    public Object getChild(int groupPosition, int childPosition) {
      return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
      return children[groupPosition].length;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      TextView textView = lavTextView();
      textView.setText(getChild(groupPosition, childPosition).toString());
      return textView;
    }

    public Object getGroup(int groupPosition) {
      return groups[groupPosition];
    }

    public int getGroupCount() {
      return groups.length;
    }

    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      TextView textView = lavTextView();
      textView.setText(getGroup(groupPosition).toString());
      return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }

    public boolean hasStableIds() {
      return true;
    }
  }

  @Override
  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
    String tekst = children[groupPosition][childPosition];
    Toast.makeText(this, "Klik på gruppe " + groupPosition + " nummer " + childPosition + ": " + tekst, Toast.LENGTH_SHORT).show();
    return true;
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    menu.setHeaderTitle("Sample menu");
    menu.add(0, 0, 0, "hej");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

    String title = ((TextView) info.targetView).getText().toString();

    int type = ExpandableListView.getPackedPositionType(info.packedPosition);
    if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
      int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
      int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
      Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos, Toast.LENGTH_SHORT).show();
      return true;
    } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
      int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
      Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
      return true;
    }

    return false;
  }
}
