package com.sk.pda.parts.want.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sk.pda.R;
import com.sk.pda.base.bean.ItemBean;
import com.sk.pda.parts.want.sql.WantOrderModelDao;
import com.sk.pda.utils.ACache;
import com.sk.pda.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.sk.pda.utils.Unitl.isInteger;


public class TypeRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private String TAG = "rightadapter";

    /**
     * 商品列表数据
     */
    private List<ItemBean> productList;
    private int max_count = 20;//最大显示数
    private Boolean isFootView = false;//是否添加了FootView
    private String tempdbname;

    private final LayoutInflater mLayoutInflater;

    //两个final int类型表示ViewType的两种类型
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;

    RecyclerView rv_right;

    /**
     * 继承父类构造函数
     *
     * @param mContext            窗体上下文
     * @param ordinaryProductList 普通数据
     */
    public TypeRightAdapter(Context mContext, List<ItemBean> ordinaryProductList, final RecyclerView rvright,String currentOrderDbName) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        productList = ordinaryProductList;
        this.rv_right = rvright;
        this.tempdbname = currentOrderDbName;
    }

    public TypeRightAdapter(Context mContext, List<ItemBean> ordinaryProductList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        productList = ordinaryProductList;
    }

    @Override
    public OrdinaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal_views = mLayoutInflater.inflate(R.layout.item_want_ordinary_right, null);
        View foot_view = mLayoutInflater.inflate(R.layout.foot_view, null);
        if (viewType == FOOT_TYPE)
            return new OrdinaryViewHolder(foot_view, FOOT_TYPE, mContext);
        return new OrdinaryViewHolder(normal_views, NORMAL_TYPE, mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果footview存在，并且当前位置ViewType是FOOT_TYPE
        if (getItemViewType(position) == FOOT_TYPE) {
            isFootView = true;
            // 刷新太快 所以使用Hanlder延迟两秒
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    max_count += 10;
                    //加载后滚动
                    rv_right.scrollToPosition(position - 1);
                }
            }, 1000);
        } else {
            OrdinaryViewHolder ordinaryViewHolder = (OrdinaryViewHolder) holder;
            ordinaryViewHolder.setData(productList.get(position), position);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == max_count - 1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public int getItemCount() {
        if (productList.size() < max_count) {
            return productList.size();
        }
        return max_count;
    }

    class OrdinaryViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;


        public TextView tvFootView;//footView的TextView属于独自的一个layout
        private ImageView iv_ordinary_right;
        private TextView tv_ordinary_itemname;
        private ImageView iv_ordinary_ishot;
        private TextView tv_ordinary_barcode;
        private TextView tv_ordinary_id;

        private TextView tv_ordinary_min_num;

        private EditText et_ordinary_num;
        private ImageView iv_ordinary_numAdd;
        private ImageView iv_ordinary_numSub;
        private ImageView iv_ordinary_addProduct;

        private TextView tv_ordinary_spec;
        private TextView tv_ordinary_unit;
        private TextView tv_ordinary_capacity;

        private TextView tv_ordinary_purprice;
        private TextView tv_ordinary_rprice;

        private TextView tv_ordinary_had_add_text;
        private TextView tv_ordinary_had_add_num;

        ACache aCache;


        public OrdinaryViewHolder(View itemView, int viewType, final Context mContext) {
            super(itemView);
            this.mContext = mContext;

            if (viewType == NORMAL_TYPE) {
                iv_ordinary_right = itemView.findViewById(R.id.want_iv_right);
                tv_ordinary_itemname = itemView.findViewById(R.id.want_tv_right_name);
                iv_ordinary_ishot = itemView.findViewById(R.id.want_iv_right_ishot);
                tv_ordinary_barcode = itemView.findViewById(R.id.want_tv_right_no);
                tv_ordinary_id = itemView.findViewById(R.id.want_tv_right_id);

                tv_ordinary_purprice = itemView.findViewById(R.id.want_tv_right_purprice);
                tv_ordinary_rprice = itemView.findViewById(R.id.want_tv_right_rprice);
                tv_ordinary_had_add_num = itemView.findViewById(R.id.want_tv_had_add);
                tv_ordinary_had_add_text = itemView.findViewById(R.id.want_tv_had_text);

                et_ordinary_num = itemView.findViewById(R.id.want_et_item_num);
                iv_ordinary_numAdd = itemView.findViewById(R.id.want_iv_item_numAdd);
                iv_ordinary_numSub = itemView.findViewById(R.id.want_iv_item_numSub);
                iv_ordinary_addProduct = itemView.findViewById(R.id.want_iv_item_add_cart);

                tv_ordinary_min_num = itemView.findViewById(R.id.want_tv_right_min);
                tv_ordinary_spec = itemView.findViewById(R.id.want_tv_right_spec);
                tv_ordinary_capacity = itemView.findViewById(R.id.want_tv_right_capacity);
                tv_ordinary_unit = itemView.findViewById(R.id.want_tv_right_unit);


            } else if (viewType == FOOT_TYPE) {
                tvFootView = (TextView) itemView;
            }

        }

        public void setData(final ItemBean itemBean, final int position) {

            aCache = ACache.get(mContext, "main");
            itemBean.setQty("0");

            //加载图片
            if (aCache.getAsBitmap(itemBean.getFigure()) != null) {
                iv_ordinary_right.setImageBitmap(aCache.getAsBitmap(itemBean.getFigure()));
                Log.e(TAG, "加载了本地缓存中的图片");
            } else {
                Glide.with(mContext)
                        .load(Constants.BASE_URl_IMAGE + itemBean.getFigure())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                iv_ordinary_right.setImageBitmap(resource);
                                aCache.put(itemBean.getFigure(), resource);
                                Log.e(TAG, "加载了网络上的图片");
                            }
                        });
            }

            //设置名称
            tv_ordinary_itemname.setText(itemBean.getItemname());

            //是否显示热卖图标
            if (!itemBean.getIshot().equals("0")) {
                iv_ordinary_ishot.setVisibility(View.VISIBLE);
            }
            tv_ordinary_barcode.setText(itemBean.getBarcode());
            tv_ordinary_id.setText(itemBean.getItemcode());
            //进价
            tv_ordinary_purprice.setText(itemBean.getDoublePurprice());
            //售价
            tv_ordinary_rprice.setText(itemBean.getDoubleRprice());


            //已添加数量
            et_ordinary_num.setText(itemBean.getQty());

            tv_ordinary_capacity.setText(itemBean.getCapacity());
            tv_ordinary_spec.setText(itemBean.getPacksize());
            tv_ordinary_unit.setText(itemBean.getStockunit());
            tv_ordinary_min_num.setText(itemBean.getMinmpoqty());
            ////////////////////////
            //设置EditText打开数字键盘
            et_ordinary_num.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型
            //可以点击
            et_ordinary_num.setFocusableInTouchMode(true);
            //可以获取焦点
            et_ordinary_num.setFocusable(true);
            //打开后全选编辑栏
            et_ordinary_num.setSelectAllOnFocus(true);


            /////////////////////////////////////////////
            //设置已添加数量
            ItemBean findBean = (new WantOrderModelDao()).querySingleData(mContext, tempdbname, itemBean.getItemcode());

            if (findBean.getItemname() != null) {
                //显示已添加字样
                tv_ordinary_had_add_text.setVisibility(View.VISIBLE);
                //显示已添加的数量
                tv_ordinary_had_add_num.setVisibility(View.VISIBLE);
                tv_ordinary_had_add_num.setText(findBean.getQty());
            }

            //设置最小订货量
            tv_ordinary_min_num.setText(itemBean.getMinmpoqty());

            //点击增加
            iv_ordinary_numAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newnum;
                    int num = Integer.parseInt(et_ordinary_num.getText().toString());
                    //散装食品默认加1
                    if (itemBean.getMinmpoqty().equals("0")) {
                        newnum = num + 1;
                    } else {
                        newnum = num + Integer.parseInt(itemBean.getMinmpoqty());
                    }
                    et_ordinary_num.setText(Integer.toString(newnum));
                    itemBean.setQty(et_ordinary_num.getText().toString());
                }
            });

            //点击减小
            iv_ordinary_numSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newnum;
                    int num = Integer.parseInt(et_ordinary_num.getText().toString());
                    //散装食品默认加1
                    if (itemBean.getMinmpoqty().equals("0")) {
                        newnum = num + 1;
                    } else {
                        if (num > 0) {
                            newnum = num - Integer.parseInt(itemBean.getMinmpoqty());

                        } else {
                            newnum = 0;
                        }
                    }
                    et_ordinary_num.setText(Integer.toString(newnum));
                    itemBean.setQty(et_ordinary_num.getText().toString());

                }
            });


            //添加进购物车
            iv_ordinary_addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //检查判断
                    String number = et_ordinary_num.getText().toString();
                    boolean isNum = isInteger(number);

                    //判断是否为正整数
                    if (!isNum) {
                        Toast.makeText(mContext, "添加的值非正整数", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //判断是否为0
                    if (number.equals("0")) {
                        Toast.makeText(mContext, "添加的值为0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //判断是否大于最小起订量
                    if (Integer.parseInt(itemBean.getMinmpoqty()) > 0) {
                        if (Integer.parseInt(number) < Integer.parseInt(itemBean.getMinmpoqty())) {
                            Toast.makeText(mContext, "小于最小起订量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //调整为整除
                    int num = Integer.parseInt(number);
                    int div;
                    if (Integer.parseInt(itemBean.getMinmpoqty()) > 0) {
                        div = Integer.parseInt(itemBean.getMinmpoqty());
                    } else {
                        div = 1;
                    }

                    //取余数
                    int ali = num % div;
                    int newnum = num - ali;

                    //如果余数不等于零
                    if (ali != 0) {
                        //算出整数
                        //弹出对话框,提示新数字
                        final AlertDialog.Builder toIntegerDialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                        toIntegerDialog.setTitle("提示");
                        toIntegerDialog.setMessage("要货的数量不符合系统规则，数量将从" + Integer.toString(num) + "调整为" + Integer.toString(newnum) + "并添加");
                        toIntegerDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //调整EditText中的字符为新的数字
                                insertData(newnum, itemBean, position, tv_ordinary_had_add_num);
                            }
                        });

                        toIntegerDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        toIntegerDialog.show();
                    } else {
                        insertData(num, itemBean, position, tv_ordinary_had_add_num);
                    }

                }
            });
        }



        private void insertData(int num, final ItemBean itemBean, int position, TextView tv_had_add_num) {
            //开始执行插入
            List<ItemBean> itemBeanList = new ArrayList<ItemBean>();
            ItemBean newitemben = new ItemBean();
            newitemben = itemBean;
            newitemben.setQty(Integer.toString(num));

            final ItemBean findBean = (new WantOrderModelDao()).querySingleData(mContext, tempdbname, itemBean.getItemcode());
            //查看数据库中是否已有这个数据
            if (findBean.getItemcode() == null) {
                //如果没有,直接添加
                itemBeanList.add(newitemben);
                boolean insertFlag = (new WantOrderModelDao()).insertOrderModelToDb(mContext, tempdbname, itemBeanList);
                if (insertFlag) {
                    Toast.makeText(mContext, "添加入了要货推车", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(position);

                } else {
                    Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                final String addnum = et_ordinary_num.getText().toString();
                final String newnum = Integer.toString(Integer.parseInt(findBean.getQty()) + Integer.parseInt(addnum));
                //弹出对话框,提示新数字
                final AlertDialog.Builder toIntegerDialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                toIntegerDialog.setTitle("提示");
                toIntegerDialog.setMessage("要货推车中已存在该产品" + findBean.getQty() + "件，继续添加为" + newnum + "件或修改为" + addnum);
                toIntegerDialog.setNegativeButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String queryString = itemBean.getItemcode();
                        int updateFlag = (new WantOrderModelDao()).updateSingleDataNum(mContext, tempdbname, queryString, newnum);
                        if (updateFlag > 0) {
                            Toast.makeText(mContext, "添加入了要货推车", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                toIntegerDialog.setNeutralButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String queryString = itemBean.getItemcode();
                        int updateFlag = (new WantOrderModelDao()).updateSingleDataNum(mContext, tempdbname, queryString, addnum);
                        if (updateFlag > 0) {
                            Toast.makeText(mContext, "已修改并添加入要货推车", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(mContext, "数量修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                toIntegerDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                toIntegerDialog.show();
            }
        }

    }
}
