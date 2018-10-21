package io.github.sithengineer.motoqueiro.data

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem

class DataUploadJobScheduler : JobScheduler() {
  override fun schedule(job: JobInfo): Int {
    return JobScheduler.RESULT_FAILURE
  }

  override fun enqueue(job: JobInfo, work: JobWorkItem): Int {
    return JobScheduler.RESULT_FAILURE
  }

  override fun cancel(jobId: Int) {

  }

  override fun cancelAll() {

  }

  override fun getAllPendingJobs(): List<JobInfo> {
    return emptyList()
  }

  override fun getPendingJob(jobId: Int): JobInfo? {
    return null
  }
}
