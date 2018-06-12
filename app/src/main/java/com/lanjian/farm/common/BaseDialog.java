package com.lanjian.farm.common;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.lanjian.farm.R;

/**
 * 
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 * 
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a> antoine vianey
 *
 */
public class BaseDialog extends Dialog {

	public BaseDialog(Context context, int theme) {
		super(context, theme);
	}

	public BaseDialog(Context context) {
		super(context);
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Context context;
		private OnShowDialogListener listener;
		private int xmlLayoutId;
		private boolean OnTouchOutside;
		//必须调用这个构造函数，而且要传入监听方法，所有的操作在监听方法那里执行,xmllayout为布局的id，OnTouchOutside点击外面是否会消息，true为会消失
		public Builder(Context context,int xmlLayoutId,Boolean OnTouchOutside,OnShowDialogListener onShowDialogListener) {
			this.context = context;
			this.listener = onShowDialogListener;
			this.xmlLayoutId = xmlLayoutId;
			this.OnTouchOutside = OnTouchOutside;
		}



		public BaseDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final BaseDialog dialog = new BaseDialog(context, 
					R.style.Dialog);
			dialog.setCanceledOnTouchOutside(OnTouchOutside);
			//布局页面
			View layout = inflater.inflate(xmlLayoutId, null);
			if (listener!=null) {
				Log.e("tag", "layout");
				listener.onShowDialog(layout);
				Log.e("tag", "basedialog");
			}
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setContentView(layout);
			return dialog;
		}

		public interface OnShowDialogListener{
			void onShowDialog(View layout);
		}
		
	}

}