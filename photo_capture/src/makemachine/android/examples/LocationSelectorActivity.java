package makemachine.android.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class LocationSelectorActivity extends Activity {

    protected static final String TAG = "LocationSelectorActivity";
    private LinearLayout mIndexerLayout;
    private ListView mListView;
    private RelativeLayout mSectionToastLayout;
    private TextView mSectionToastText;
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private SectionIndexer mIndexer;
    private int lastSelectedPosition = -1;

    private ArrayList<String> mItems;
    private ArrayList<LocationModel> locationModels;
    private ArrayList<String> selectedModels = new ArrayList<String>();

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_selector_ui);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();
    }

    private void initView() {
        mIndexerLayout = (LinearLayout) findViewById(R.id.indexer_layout);
        mListView = (ListView) findViewById(R.id.contacts_list);
        mSectionToastLayout = (RelativeLayout) findViewById(R.id.section_toast_layout);
        mSectionToastText = (TextView) findViewById(R.id.section_toast_text);
        for(int i = 0; i < alphabet.length(); i++) {
            TextView letterTextView = new TextView(this);
            letterTextView.setText(alphabet.charAt(i)+"");
            letterTextView.setTextSize(12f);
            letterTextView.setGravity(Gravity.CENTER);
            LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
            letterTextView.setLayoutParams(params);
            letterTextView.setPadding(4, 2, 4, 2);
            letterTextView.setTextColor(getResources().getColor(R.color.location_selector_text_color));
            letterTextView.setTypeface(Typeface.DEFAULT_BOLD);
            mIndexerLayout.addView(letterTextView);
            mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
        }

        locationModels = new ArrayList<LocationModel>();
        locationModels.add(new LocationModel("India"));
        locationModels.add(new LocationModel("French Polynesia"));
        locationModels.add(new LocationModel("Yemen"));
        locationModels.add(new LocationModel("Malaysia"));
        locationModels.add(new LocationModel("Congo"));
        locationModels.add(new LocationModel("Romania"));
        locationModels.add(new LocationModel("Belize"));
        locationModels.add(new LocationModel("Yemen"));
        locationModels.add(new LocationModel("Zambia"));
        locationModels.add(new LocationModel("Zimbabwe"));
        locationModels.add(new LocationModel("Mali"));
        locationModels.add(new LocationModel("Macau"));
        locationModels.add(new LocationModel("Madagascar"));
        locationModels.add(new LocationModel("Djibouti"));
        locationModels.add(new LocationModel("Czech Republic"));
        locationModels.add(new LocationModel("Dominica"));
        locationModels.add(new LocationModel("Cyprus"));
        locationModels.add(new LocationModel("France"));
        locationModels.add(new LocationModel("Croatia"));
        locationModels.add(new LocationModel("Rwanda"));
        locationModels.add(new LocationModel("Chile"));
        
        Collections.sort(locationModels);

        LocationSelectorAdapter adapter1 = new LocationSelectorAdapter(this, locationModels);
        mListView.setAdapter(adapter1);
        mIndexer = (SectionIndexer) adapter1;

        mIndexerLayout.setOnTouchListener(mOnTouchListener);

        final EditText searchText = (EditText) findViewById(R.id.EditText01);

        searchText.addTextChangedListener(new TextWatcher()
        {
            private int mSearchTextLength;
            private ArrayList<LocationModel> array_sort= new ArrayList<LocationModel>();

            public void afterTextChanged(Editable s) {
                if(mSearchTextLength <= 0) {
                    searchText.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_action_search), null, null, null);
                }
            }
            public void beforeTextChanged(CharSequence s,
                    int start, int count, int after) {
                // Abstract Method of TextWatcher Interface.
            }
            public void onTextChanged(CharSequence s,
                    int start, int before, int count) {
                mSearchTextLength = searchText.getText().length();
                array_sort.clear();
                
                if(mSearchTextLength > 0) {
                    searchText.setCompoundDrawables(null, null, null, null);
                }
                
                for (int i = 0; i < locationModels.size(); i++) {
                    if (mSearchTextLength <= locationModels.get(i).getName().length()) {
                        if(searchText.getText().toString().equalsIgnoreCase(
                                (String)
                                locationModels.get(i).getName().subSequence(0,
                                        mSearchTextLength))) {
                            array_sort.add(locationModels.get(i));
                        }
                    }
                } 
                mListView.setAdapter(new LocationSelectorAdapter(LocationSelectorActivity.this, array_sort));
                if(array_sort.size() == locationModels.size()) {
                    mIndexerLayout.setVisibility(View.VISIBLE);
                } else {
                    mIndexerLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float alphabetHeight = mIndexerLayout.getHeight();
            float y = event.getY();
            int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));
            if (sectionPosition < 0) {
                sectionPosition = 0;
            } else if (sectionPosition > 26) {
                sectionPosition = 26;
            }
            if(lastSelectedPosition != sectionPosition) {
                if(-1 != lastSelectedPosition){
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                lastSelectedPosition = sectionPosition;
            }
            String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
            int position = mIndexer.getPositionForSection(sectionPosition);
            TextView textView = (TextView) mIndexerLayout.getChildAt(sectionPosition);

            if(textView.getText().equals(sectionLetter)) {
                Log.d("", "");
                textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
            }
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                mSectionToastLayout.setVisibility(View.VISIBLE);
                mSectionToastText.setText(sectionLetter);
                mListView.setSelection(position);
                break;
            case MotionEvent.ACTION_MOVE:
                mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                mSectionToastLayout.setVisibility(View.VISIBLE);
                mSectionToastText.setText(sectionLetter);
                mListView.setSelection(position);
                break;
            case MotionEvent.ACTION_UP:
                //mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mSectionToastLayout.setVisibility(View.GONE);
            default:
                mSectionToastLayout.setVisibility(View.GONE);
                break;
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putStringArrayListExtra(PhotoCaptureExample.SELECTED_NO_OF_LOCATIONS,  selectedModels);
        setResult(Activity.RESULT_OK, data);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        return super.onOptionsItemSelected(item);
    }

    public class LocationSelectorAdapter extends ArrayAdapter<LocationModel> implements SectionIndexer {

        private final List<LocationModel> list;
        private final Activity context;

        public LocationSelectorAdapter(Activity context, List<LocationModel> list) {
            super(context, R.layout.location_selection_cell, list);
            this.context = context;
            this.list = list;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                view = inflator.inflate(R.layout.location_selection_cell, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) view.findViewById(R.id.label);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
                viewHolder.checkbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        LocationModel element = (LocationModel) viewHolder.checkbox.getTag();
                        boolean checked = buttonView.isChecked();
                        element.setSelected(checked);
                        if(checked) {
                            if(!selectedModels.contains(element.getName())) {
                                selectedModels.add(element.getName());
                            }
                        }
                    }
                });
                view.setTag(viewHolder);
                viewHolder.checkbox.setTag(list.get(position));
            } else {
                view = convertView;
                ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.text.setText(list.get(position).getName());
            holder.checkbox.setChecked(list.get(position).isSelected());
            return view;
        }

        @Override
        public int getPositionForSection(int section) {
            // If there is no item for current section, previous section will be selected
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).getName().charAt(0)), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(String.valueOf(getItem(j).getName().charAt(0)), String.valueOf(alphabet.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[alphabet.length()];
            for (int i = 0; i < alphabet.length(); i++)
                sections[i] = String.valueOf(alphabet.charAt(i));
            return sections;
        }
    }

    private class LocationModel implements Comparable<LocationModel> {
        private String mName;
        private boolean selected;

        public LocationModel(String name) {
            mName = name;
            selected = false;
        }

        public String getName() {
            return mName;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int compareTo(LocationModel model) {
            int result = mName.compareTo(model.getName());
            return result;
        }
    }
}