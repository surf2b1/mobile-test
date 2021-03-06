package com.jaredrummler.mobiletest.ui.qrcode

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jaredrummler.mobiletest.R
import com.jaredrummler.mobiletest.networking.FailedResult
import com.jaredrummler.mobiletest.networking.Result
import com.jaredrummler.mobiletest.networking.SeedResult
import com.jaredrummler.mobiletest.ui.BaseFragment
import com.jaredrummler.mobiletest.ui.CountDownTextView
import kotlinx.android.synthetic.main.fragment_qr_code.countDownTextView
import kotlinx.android.synthetic.main.fragment_qr_code.qrCodeImage

class QrCodeFragment : BaseFragment(), QrCodeView, CountDownTextView.Listener {

  lateinit var viewModel: QrCodeViewModel

  override fun getActionBarTitle(): Int = R.string.qr_code

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(QrCodeViewModel::class.java)
  }

  override fun onPause() {
    super.onPause()
    countDownTextView?.cancel()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_qr_code, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.seed.observe(this, Observer<Result> { result ->
      if (result is FailedResult) {
        showError()
      } else if (result is SeedResult) {
        showResult(result)
      }
    })

    if (savedInstanceState == null) { // Don't reload seed on orientation change
      viewModel.loadSeed()
    }
  }

  override fun showResult(result: SeedResult) {
    qrCodeImage.setImageBitmap(result.qrcode)
    countDownTextView.startCountDown(result.expiredTime, this@QrCodeFragment)
  }

  override fun showError() {
    Toast.makeText(requireContext(), R.string.msg_result_failed_to_load, Toast.LENGTH_LONG).show()
  }

  override fun onCountDownComplete() {
    viewModel.loadSeed()
  }

  companion object {
    fun newInstance() = QrCodeFragment()
  }

}