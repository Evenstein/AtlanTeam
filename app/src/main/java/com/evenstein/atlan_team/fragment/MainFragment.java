package com.evenstein.atlan_team.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.evenstein.atlan_team.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 *      This fragment represents main screen of app. There are 5 {@code CardView}'s on each of next
 *   item, which requests from {@code https://jsonplaceholder.typicode.com/}: {@code posts},
 *   {@code comments}, {@code users}, {@code photos} and {@code todos}. Into {@code posts} and
 *   {@code comments} has been realized search by ID's. Into {@code users} shown 5 first users (sorted
 *   by ID). Into {@code photos} shown image with ID = 3. Into {@code todos} shown random task.
 *
 *  @author Sokolsky Vitaly
 *  @version 1.0
 *
 */

public class MainFragment extends Fragment {

    List mUsers;

    TextView mPostsName, mPostsBody, mPostId, mPostAuthor;
    TextView mCommentId, mCommentPostId, mCommentsName, mCommentEmail, mCommentsBody;
    TextView mUser1, mUser2, mUser3, mUser4, mUser5;
    TextView mPhotoTitle;
    ImageView mPhotoThumb;
    TextView mTodoId, mTodoTitle, mTodoComplete;
    EditText mFindPostId, mFindCommentId;
    Button mFindPost, mFindComment;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initFields(view);
        setButtonsClickListeners();

        Random random = new Random();
        requestInfo("users", (random.nextInt(5) + 1));
        requestInfo("comments", (random.nextInt(500) + 1));
        requestInfo("photos", 3);
        requestInfo("todos", (random.nextInt(200) + 1));
        requestInfo("posts", (random.nextInt(100) + 1));
        return view;
    }

    public void initFields(View view) {
        mPostsName = (TextView) view.findViewById(R.id.posts_title);
        mPostsBody = (TextView) view.findViewById(R.id.posts_body);
        mPostId = (TextView) view.findViewById(R.id.post_id);
        mPostAuthor = (TextView) view.findViewById(R.id.author_name);
        mFindPostId = (EditText) view.findViewById(R.id.edit_post_id);
        mFindPost = (Button) view.findViewById(R.id.find_post);

        mCommentId = (TextView) view.findViewById(R.id.comment_id);
        mCommentPostId = (TextView) view.findViewById(R.id.comment_post_id);
        mCommentsName = (TextView) view.findViewById(R.id.comment_name);
        mCommentEmail = (TextView) view.findViewById(R.id.comment_email);
        mCommentsBody = (TextView) view.findViewById(R.id.comments_body);
        mFindCommentId = (EditText) view.findViewById(R.id.edit_comment_id);
        mFindComment = (Button) view.findViewById(R.id.find_comment);

        mUser1 = (TextView) view.findViewById(R.id.user1);
        mUser2 = (TextView) view.findViewById(R.id.user2);
        mUser3 = (TextView) view.findViewById(R.id.user3);
        mUser4 = (TextView) view.findViewById(R.id.user4);
        mUser5 = (TextView) view.findViewById(R.id.user5);

        mPhotoTitle = (TextView) view.findViewById(R.id.photo_title);
        mPhotoThumb = (ImageView) view.findViewById(R.id.photo_thumbnail);

        mTodoId = (TextView) view.findViewById(R.id.todo_id);
        mTodoTitle = (TextView) view.findViewById(R.id.todo_title);
        mTodoComplete = (TextView) view.findViewById(R.id.todo_status);
    }

    private void requestInfo(String type, int id) {
        String uri = "https://jsonplaceholder.typicode.com/" + type + "/" + id;
        RequestQueue queue;
        JsonObjectRequest jsObjRequest;
        final ProgressDialog progressDialog;
        switch (type) {

            case "posts":
                queue = Volley.newRequestQueue(getContext());
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading. Please wait...");
                progressDialog.show();
                jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                mPostId.setText(response.optString("id"));
                                mPostsName.setText(response.optString("title"));
                                mPostsBody.setText(response.optString("body"));
                                mPostAuthor.setText(response.optString("userId"));
                                progressDialog.dismiss();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                            }
                        });
                queue.add(jsObjRequest);
                break;

            case "comments":
                queue = Volley.newRequestQueue(getContext());
                jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                mCommentId.setText(response.optString("id"));
                                mCommentPostId.setText(response.optString("postId"));
                                mCommentsName.setText(response.optString("name"));
                                mCommentEmail.setText(response.optString("email"));
                                mCommentsBody.setText(response.optString("body"));
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                queue.add(jsObjRequest);
                break;

            case "users":
                queue = Volley.newRequestQueue(getContext());
                mUsers = new ArrayList<>();
                String url = "https://jsonplaceholder.typicode.com/" + type;
                JsonArrayRequest jsArrRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        mUsers.add(response.getJSONObject(i));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    String user1 = ((JSONObject) mUsers.get(0)).optString("id")
                                            + ". Name- " + ((JSONObject) mUsers.get(0)).optString("name")
                                            + ", username- "
                                            + ((JSONObject) mUsers.get(0)).optString("username");
                                    mUser1.setText(user1);

                                    String user2 = ((JSONObject) mUsers.get(1)).optString("id")
                                            + ". Name- " + ((JSONObject) mUsers.get(1)).optString("name")
                                            + ", username- "
                                            + ((JSONObject) mUsers.get(1)).optString("username");
                                    mUser2.setText(user2);

                                    String user3 = ((JSONObject) mUsers.get(2)).optString("id")
                                            + ". Name- " + ((JSONObject) mUsers.get(2)).optString("name")
                                            + ", username- "
                                            + ((JSONObject) mUsers.get(2)).optString("username");
                                    mUser3.setText(user3);

                                    String user4 = ((JSONObject) mUsers.get(3)).optString("id")
                                            + ". Name- " + ((JSONObject) mUsers.get(3)).optString("name")
                                            + ", username- "
                                            + ((JSONObject) mUsers.get(3)).optString("username");
                                    mUser4.setText(user4);

                                    String user5 = ((JSONObject) mUsers.get(4)).optString("id")
                                            + ". Name- " + ((JSONObject) mUsers.get(4)).optString("name")
                                            + ", username- "
                                            + ((JSONObject) mUsers.get(4)).optString("username");
                                    mUser5.setText(user5);
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("ErrorVolley-->", error.toString());
                            }
                        });
                queue.add(jsArrRequest);
                break;

            case "photos":
                queue = Volley.newRequestQueue(getContext());
                jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                mPhotoTitle.setText(response.optString("title"));
                                Picasso.with(getContext())
                                        .load(response.optString("thumbnailUrl"))
                                        .into(mPhotoThumb);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                queue.add(jsObjRequest);
                break;

            case "todos":
                queue = Volley.newRequestQueue(getContext());
                jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                mTodoId.setText(response.optString("id"));
                                mTodoTitle.setText(response.optString("title"));
                                String status;
                                if (response.optString("completed").equals("true"))
                                    status = "Completed";
                                else
                                    status = "in Progress";
                                mTodoComplete.setText(status);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                queue.add(jsObjRequest);
                break;

        }
    }

    public void setButtonsClickListeners() {
        mFindPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mFindPostId.getText().toString();
                if (!(id.isEmpty() || Integer.parseInt(id) < 1 || Integer.parseInt(id) > 100)) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getContext()
                                .getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mFindPostId.setCursorVisible(false);

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Find Post");
                    alert.setMessage("Are you sure want to find post # " + id);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String uri = "https://jsonplaceholder.typicode.com/posts/"
                                    + mFindPostId.getText().toString();
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                    (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            mPostId.setText(response.optString("id"));
                                            mPostsName.setText(response.optString("title"));
                                            mPostsBody.setText(response.optString("body"));
                                            mPostAuthor.setText(response.optString("userId"));
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                            queue.add(jsObjRequest);
                            mFindPostId.setText("");
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                } else if (id.isEmpty()) {
                    Toast.makeText(getContext(), "ID value can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "ID value must be in range of 1 and 100", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mFindPostId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!mFindPostId.isCursorVisible()) {
                   mFindPostId.setCursorVisible(true);
               }
            }
        });

        mFindComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mFindCommentId.getText().toString();
                if (!(id.isEmpty() || Integer.parseInt(id) < 1 || Integer.parseInt(id) > 500)) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getContext()
                                .getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mFindCommentId.setCursorVisible(false);

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Find Comment");
                    alert.setMessage("Are you sure want to find Comment # " + id);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String uri = "https://jsonplaceholder.typicode.com/comments/"
                                    + mFindCommentId.getText().toString();
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                    (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            mCommentId.setText(response.optString("id"));
                                            mCommentPostId.setText(response.optString("postId"));
                                            mCommentsName.setText(response.optString("name"));
                                            mCommentEmail.setText(response.optString("email"));
                                            mCommentsBody.setText(response.optString("body"));
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                            queue.add(jsObjRequest);
                            mFindCommentId.setText("");
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                } else if (id.isEmpty()) {
                    Toast.makeText(getContext(), "ID value can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "ID value must be in range of 1 and 500", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mFindCommentId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFindCommentId.isCursorVisible()) {
                    mFindCommentId.setCursorVisible(true);
                }
            }
        });
    }

}
