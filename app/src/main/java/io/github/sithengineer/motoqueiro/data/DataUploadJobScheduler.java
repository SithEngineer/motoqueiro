package io.github.sithengineer.motoqueiro.data;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;

public class DataUploadJobScheduler extends JobScheduler {
  @Override public int schedule(JobInfo job) {
    return 0;
  }

  @Override public void cancel(int jobId) {

  }

  @Override public void cancelAll() {

  }

  @NonNull @Override public List<JobInfo> getAllPendingJobs() {
    return null;
  }

  @Nullable @Override public JobInfo getPendingJob(int jobId) {
    return null;
  }
}
