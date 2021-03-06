package com.wiklosoft.ocf.livingroom;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.wiklosoft.ocf.OcfControlPoint;
import com.wiklosoft.ocf.OcfDevice;
import com.wiklosoft.ocf.OcfDeviceVariableCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class LivingRoomFragment extends Fragment{
    private static final String LIVINGROOM_ID = "00000000-0000-0000-0001-000000000001";
    private String TAG = "LivingRoomFragment";
    private static final String RESOURCE_FRONT = "/lampa/front";
    private static final String RESOURCE_BACK = "/lampa/back";
    private static final String RESOURCE_TABLE = "/lampa/table";
    private static final String RESOURCE_AMBIENT = "/lampa/ambient";
    private static final String RESOURCE_AMBIENT_POWER = "/lampa/ambientPower";
    private static final String RESOURCE_MASTER = "/master";

    TextView mConnectionStatus = null;
    OcfDevice mDevice = null;
    OcfControlPoint mController = null;

    ToggleButton mMasterButton = null;
    SeekBar mFrontSeekBar = null;
    SeekBar mBackSeekBar = null;
    SeekBar mTableSeekBar = null;
    SeekBar mAmbient = null;

    ImageButton mSearchForDevices = null;
    ImageButton mTurnOnButton = null;

    ColorPicker mPicker = null;

    void setScrollListeners() {
        mFrontSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mDevice != null && fromUser) {
                    String v = "{\"dimmingSetting\": " + Integer.toString(progress) + "}";

                    mDevice.post(RESOURCE_FRONT, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d("VariableListAdapter", "value set");
                        }
                    });
                    mMasterButton.setChecked(true);
                }
            }
        });
        mBackSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mDevice != null && fromUser) {
                    String v = "{\"dimmingSetting\": " + Integer.toString(progress) + "}";

                    mDevice.post(RESOURCE_BACK, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d("VariableListAdapter", "value set");
                        }
                    });
                    mMasterButton.setChecked(true);
                }
            }
        });
        mTableSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mDevice != null && fromUser) {
                    String v = "{\"dimmingSetting\": " + Integer.toString(progress) + "}";

                    mDevice.post(RESOURCE_TABLE, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d("VariableListAdapter", "value set");
                        }
                    });
                    mMasterButton.setChecked(true);
                }
            }
        });

        mAmbient.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mDevice != null && fromUser) {
                    String v = "{\"dimmingSetting\": " + Integer.toString(progress) + "}";

                    mDevice.post(RESOURCE_AMBIENT_POWER, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d("VariableListAdapter", "value set");
                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i, boolean fromUser) {
                if (mDevice != null && fromUser) {
                    int red = Color.red(mPicker.getColor());
                    int green = Color.green(mPicker.getColor());
                    int blue = Color.blue(mPicker.getColor());

                    String v = "{\"dimmingSetting\": \"" + red + "," + green + "," + blue + "\"}";

                    mDevice.post(RESOURCE_AMBIENT, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d("VariableListAdapter", "value set");
                        }
                    });
                }
            }
        });
    }
    OcfDeviceVariableCallback mMasterUpdateCallback = new OcfDeviceVariableCallback() {
        @Override
        public void update(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject value = null;
                    try {
                        value = new JSONObject(json);

                        if (value.getBoolean("value")){
                            turnOn();
                        }else{
                            turnOff();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


    OcfDeviceVariableCallback mFrontUpdateCallback = new OcfDeviceVariableCallback() {
        @Override
        public void update(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject value = null;
                    try {
                        value = new JSONObject(json);
                        mFrontSeekBar.setProgress(value.getInt("dimmingSetting"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    OcfDeviceVariableCallback mBackUpdateCallback = new OcfDeviceVariableCallback() {
        @Override
        public void update(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject value = null;
                    try {
                        value = new JSONObject(json);
                        mBackSeekBar.setProgress(value.getInt("dimmingSetting"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    OcfDeviceVariableCallback mTableUpdateCallback = new OcfDeviceVariableCallback() {
        @Override
        public void update(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject value = null;
                    try {
                        value = new JSONObject(json);
                        mTableSeekBar.setProgress(value.getInt("dimmingSetting"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    OcfDeviceVariableCallback mAmbientPowerUpdateCallback = new OcfDeviceVariableCallback() {
        @Override
        public void update(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject value = null;
                    try {
                        value = new JSONObject(json);
                        mAmbient.setProgress(value.getInt("dimmingSetting"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    OcfDeviceVariableCallback mAmbientUpdateCallback = new OcfDeviceVariableCallback() {
        @Override
        public void update(final String json) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject value = null;
                    try {
                        value = new JSONObject(json);
                        String[] colors = value.getString("dimmingSetting").split(",");
                        if (colors.length == 3) {
                            int red = Integer.valueOf(colors[0]);
                            int green = Integer.valueOf(colors[1]);
                            int blue = Integer.valueOf(colors[2]);

                            mPicker.setColor(Color.rgb(red, green, blue));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    OcfControlPoint.OcfOnDeviceFound onFound = new OcfControlPoint.OcfOnDeviceFound() {
        @Override
        public void deviceFound(OcfDevice dev) {
            if (dev.getDi().equals(LIVINGROOM_ID) && mDevice == null){
                Log.d("", "device Found");
                mDevice = dev;

                mDevice.observe(RESOURCE_FRONT, mFrontUpdateCallback);
                mDevice.observe(RESOURCE_BACK, mBackUpdateCallback);
                mDevice.observe(RESOURCE_TABLE, mTableUpdateCallback);
                mDevice.observe(RESOURCE_AMBIENT, mAmbientUpdateCallback);
                mDevice.observe(RESOURCE_AMBIENT_POWER, mAmbientUpdateCallback);
                mDevice.observe(RESOURCE_MASTER, mMasterUpdateCallback);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectionStatus.setText("Connected");
                    }
                });
            }
        }
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.living_room_fragment, container, false);

        mController = ((OcfApplicationContext)getActivity().getApplicationContext()).getOcfControlPoint();
        mController.addOnDeviceFoundCallback(onFound);
        mController.searchDevices();
        
        mConnectionStatus = (TextView) rootView.findViewById(R.id.device_connected);
        mMasterButton = (ToggleButton) rootView.findViewById(R.id.master_button);

        mFrontSeekBar = (SeekBar) rootView.findViewById(R.id.output_front);
        mBackSeekBar =(SeekBar) rootView.findViewById(R.id.output_back);
        mTableSeekBar = (SeekBar) rootView.findViewById(R.id.output_table);

        mFrontSeekBar.setMax(255);
        mBackSeekBar.setMax(255);
        mTableSeekBar.setMax(255);

        mSearchForDevices = (ImageButton) rootView.findViewById(R.id.searchForDevices);
        mSearchForDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (mController != null) {
                   mController.searchDevices();
               }
            }
        });


        mAmbient = (SeekBar) rootView.findViewById(R.id.output_color);
        mAmbient.setProgress(100);

        mPicker = (ColorPicker) rootView.findViewById(R.id.picker);

        ViewTreeObserver vto = mPicker.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPicker.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int a = (int) (mPicker.getHeight() *0.45);
                mMasterButton.getLayoutParams().width = a;
                mMasterButton.getLayoutParams().height = a;
            }
        });

        setScrollListeners();

        mMasterButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mDevice != null) {
                    String v = "{\"value\": false}";

                    mDevice.post(RESOURCE_MASTER, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d(TAG, "value set");
                        }
                    });
                    turnOff();
                }
            }
        });

        mTurnOnButton = (ImageButton) rootView.findViewById(R.id.turnOnButton);
        mTurnOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDevice != null) {
                    String v = "{\"value\": true}";

                    mDevice.post(RESOURCE_MASTER, v, new OcfDeviceVariableCallback() {
                        @Override
                        public void update(String json) {
                            Log.d(TAG, "value set");
                        }
                    });
                    turnOn();
                }
            }
        });

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        return rootView;
    }

    void turnOn(){
        Log.d(TAG, "turnOn");
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.screenBrightness = 50;
        getActivity().getWindow().setAttributes(params);

        mTurnOnButton.setVisibility(View.GONE);
    }
    void turnOff(){
        Log.d(TAG, "turnOff");
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.screenBrightness = 0;
        getActivity().getWindow().setAttributes(params);

        mTurnOnButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();

        mDevice.unobserve(RESOURCE_FRONT, mFrontUpdateCallback);
        mDevice.unobserve(RESOURCE_BACK, mBackUpdateCallback);
        mDevice.unobserve(RESOURCE_TABLE, mTableUpdateCallback);
        mDevice.unobserve(RESOURCE_AMBIENT, mAmbientUpdateCallback);
        mDevice.unobserve(RESOURCE_AMBIENT_POWER, mAmbientPowerUpdateCallback);
        mDevice.unobserve(RESOURCE_MASTER, mMasterUpdateCallback);
    }

}