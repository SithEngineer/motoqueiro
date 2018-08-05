package io.github.sithengineer.motoqueiro.data;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class DataUploadJobScheduler extends JobScheduler {
  @Override public int schedule(@NonNull JobInfo job) {
    return JobScheduler.RESULT_FAILURE;
  }

  @Override public int enqueue(@NonNull JobInfo job, @NonNull JobWorkItem work) {
    return JobScheduler.RESULT_FAILURE;
  }

  @Override public void cancel(int jobId) {

  }

  @Override public void cancelAll() {

  }

  @NonNull @Override public List<JobInfo> getAllPendingJobs() {
    return Collections.emptyList();
  }

  @Nullable @Override public JobInfo getPendingJob(int jobId) {
    return null;
  }
}
