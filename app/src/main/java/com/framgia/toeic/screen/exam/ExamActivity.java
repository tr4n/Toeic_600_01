package com.framgia.toeic.screen.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.framgia.toeic.R;
import com.framgia.toeic.data.model.ExamLesson;
import com.framgia.toeic.data.repository.ExamLessonRepository;
import com.framgia.toeic.data.repository.MarkRepository;
import com.framgia.toeic.data.source.local.DBHelper;
import com.framgia.toeic.data.source.local.ExamLessonDatabaseHelper;
import com.framgia.toeic.data.source.local.ExamLessonLocalDataSource;
import com.framgia.toeic.data.source.local.MarkDatabaseHelper;
import com.framgia.toeic.data.source.local.MarkLocalDataSource;
import com.framgia.toeic.screen.base.BaseActionBar;
import com.framgia.toeic.screen.exam_detail.ExamDetailActivity;

import java.util.List;

public class ExamActivity extends BaseActionBar implements ExamContract.View,
        ExamLessonAdapter.OnItemClickListener {
    private ExamLessonAdapter mAdapter;
    private ExamContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;

    public static Intent getExamActivity(Context context) {
        return new Intent(context, ExamActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_exam;
    }

    @Override
    protected void initComponent() {
        initActionBar(getResources().getString(R.string.action_exam));
        mRecyclerView = findViewById(R.id.recycler_exam);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getExams();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        ExamLessonLocalDataSource examLessonLocalDataSource = new ExamLessonLocalDataSource(
                new ExamLessonDatabaseHelper(new DBHelper(this)));

        MarkLocalDataSource markLocalDataSource = new MarkLocalDataSource(
                new MarkDatabaseHelper(new DBHelper(this))
        );

        mPresenter = new ExamPresenter(this, ExamLessonRepository.getInstance(examLessonLocalDataSource),
                MarkRepository.getInstance(markLocalDataSource));
    }

    @Override
    public void showExams(List<ExamLesson> examLessons) {
        mAdapter = new ExamLessonAdapter(examLessons, this);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.updateMark(examLessons);
    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(ExamLesson examLesson) {
        startActivity(ExamDetailActivity.getIntent(this, examLesson));
    }
}
