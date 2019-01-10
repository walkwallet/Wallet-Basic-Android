package systems.v.wallet.basic;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;

public class AlertDialog extends AppCompatDialog implements View.OnClickListener {

    private ImageView mIvIcon;
    private TextView mTvTitle;
    private TextView mTvMessage;
    private FrameLayout mFlContent;
    private Button mBtPositive;
    private Button mBtNegative;

    private Context mContext;
    private int mIconId;
    private Drawable mIcon;
    private CharSequence mTitle;
    private CharSequence mMessage;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private DialogInterface.OnClickListener mPositiveButtonListener;
    private DialogInterface.OnClickListener mNegativeButtonListener;

    private boolean mAutoDismiss;

    private AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.basic_dialog_alert);
        mIvIcon = findViewById(R.id.iv_icon);
        mTvTitle = findViewById(R.id.tv_title);
        mTvMessage = findViewById(R.id.tv_message);
        mFlContent = findViewById(R.id.fl_content);
        mBtPositive = findViewById(R.id.bt_positive);
        mBtNegative = findViewById(R.id.bt_negative);

        mBtPositive.setOnClickListener(this);
        mBtNegative.setOnClickListener(this);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_negative) {
            if (mNegativeButtonListener != null) {
                mNegativeButtonListener.onClick(this, BUTTON_NEGATIVE);
            }
            if (mAutoDismiss) {
                dismiss();
            }
        } else if (id == R.id.bt_positive) {
            if (mPositiveButtonListener != null) {
                mPositiveButtonListener.onClick(this, BUTTON_POSITIVE);
            }
            if (mAutoDismiss) {
                dismiss();
            }
        }
    }

    public void setIcon(int resId) {
        mIcon = null;
        mIconId = resId;

        if (mIvIcon != null) {
            if (resId != 0) {
                mIvIcon.setVisibility(View.VISIBLE);
                mIvIcon.setImageResource(mIconId);
            } else {
                mIvIcon.setVisibility(View.GONE);
            }
        }
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        mIconId = 0;

        if (mIvIcon != null) {
            if (icon != null) {
                mIvIcon.setVisibility(View.VISIBLE);
                mIvIcon.setImageDrawable(icon);
            } else {
                mIvIcon.setVisibility(View.GONE);
            }
        }
    }

    public void setTitle(@StringRes int titleId) {
        setTitle(mContext.getText(titleId));
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public void setMessage(@StringRes int textId) {
        setMessage(mContext.getText(textId));
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mTvMessage != null) {
            mTvMessage.setText(message);
            mFlContent.setVisibility(View.VISIBLE);
        }
    }

    public void setCustomView(View view) {
        if (mFlContent != null) {
            mFlContent.removeAllViews();
            mFlContent.addView(view);
            mFlContent.setVisibility(View.VISIBLE);
        }
    }

    public void setPositiveButton(CharSequence text, final OnClickListener listener) {
        mPositiveButtonText = text;
        mPositiveButtonListener = listener;
        mBtPositive.setText(text);
        mBtPositive.setVisibility(View.VISIBLE);
    }

    public void setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
        mNegativeButtonText = text;
        mNegativeButtonListener = listener;
        mBtNegative.setText(text);
        mBtNegative.setVisibility(View.VISIBLE);
    }

    public void setAutoDismiss(boolean auto) {
        mAutoDismiss = auto;
    }

    public static class Builder {

        private int mTheme;
        private Context mContext;
        private int mIconId;
        private Drawable mIcon;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private View mCustomView;
        private DialogInterface.OnClickListener mPositiveButtonListener;
        private DialogInterface.OnClickListener mNegativeButtonListener;
        private boolean mCanceledOnTouchOutside = true;
        private boolean mAutoDismiss = true;

        public Builder(@NonNull Context context) {
            this(context, R.style.BasicAlertDialog_Light);
        }

        public Builder(@NonNull Context context, int themeResId) {
            mContext = context;
            mTheme = themeResId;
        }

        public Builder setTitle(@StringRes int titleId) {
            mTitle = mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setCustomView(@Nullable View customView) {
            mCustomView = customView;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            mMessage = mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(@Nullable CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setIcon(@DrawableRes int iconId) {
            mIconId = iconId;
            return this;
        }

        public Builder setIcon(@Nullable Drawable icon) {
            mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(@AttrRes int attrId) {
            TypedValue out = new TypedValue();
            mContext.getTheme().resolveAttribute(attrId, out, true);
            mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, final OnClickListener listener) {
            mPositiveButtonText = mContext.getText(textId);
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, final DialogInterface.OnClickListener listener) {
            mNegativeButtonText = mContext.getText(textId);
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            mCanceledOnTouchOutside = cancel;
            return this;
        }

        public Builder autoDismiss() {
            mAutoDismiss = true;
            return this;
        }

        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(mContext, mTheme);
            if (mCustomView != null) {
                dialog.setCustomView(mCustomView);
            }
            if (mTitle != null) {
                dialog.setTitle(mTitle);
            }
            if (mIcon != null) {
                dialog.setIcon(mIcon);
            }
            if (mIconId != 0) {
                dialog.setIcon(mIconId);
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mPositiveButtonText != null) {
                dialog.setPositiveButton(mPositiveButtonText, mPositiveButtonListener);
            }
            if (mNegativeButtonText != null) {
                dialog.setNegativeButton(mNegativeButtonText, mNegativeButtonListener);
            }
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            dialog.setAutoDismiss(mAutoDismiss);
            return dialog;
        }

        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public AlertDialog info() {
            setCanceledOnTouchOutside(false);
            if (mPositiveButtonText == null) {
                setPositiveButton(R.string.basic_alert_dialog_ok, null);
            }
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public AlertDialog alert() {
            setCanceledOnTouchOutside(false);
            if (mPositiveButtonText == null) {
                setPositiveButton(R.string.basic_alert_dialog_confirm, null);
            }
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public AlertDialog confirm() {
            if (mPositiveButtonText == null) {
                setPositiveButton(R.string.basic_alert_dialog_confirm, null);
            }
            if (mNegativeButtonText == null) {
                setNegativeButton(R.string.basic_alert_dialog_cancel, null);
            }
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
