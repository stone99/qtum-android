package org.qtum.wallet.ui.fragment.profile_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.ui.fragment.about_fragment.AboutFragment;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;
import org.qtum.wallet.ui.fragment.smart_contracts_fragment.SmartContractsFragment;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.ThemeUtils;

import butterknife.BindView;

import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_FOR_PASSPHRASE;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHANGING;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public abstract class ProfileFragment extends BaseFragment implements ProfileView, LogOutDialogFragment.OnYesClickListener, OnSettingClickListener {

    @BindView(org.qtum.wallet.R.id.pref_list)
    protected RecyclerView prefList;

    protected ProfilePresenter mProfileFragmentPresenter;
    protected DividerItemDecoration dividerItemDecoration;
    private UpdateService mUpdateService;
    private LanguageChangeListener mLanguageChangeListener = new LanguageChangeListener() {
        @Override
        public void onLanguageChange() {
            resetText();
        }
    };

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ProfileFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().setIconChecked(1);
        getPresenter().setupLanguageChangeListener(mLanguageChangeListener);
    }

    @Override
    protected void createPresenter() {
        mProfileFragmentPresenter = new ProfilePresenterImpl(this, new ProfileInteractorImpl(getContext()));
    }

    @Override
    protected ProfilePresenter getPresenter() {
        return mProfileFragmentPresenter;
    }

    @Override
    public void onSettingClick(int key) {
        BaseFragment fragment = null;
        switch (key) {
            case org.qtum.wallet.R.string.language:
                fragment = LanguageFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.change_pin:
                fragment = PinFragment.newInstance(CHANGING, getContext());
                break;
            case org.qtum.wallet.R.string.wallet_backup:
                fragment = PinFragment.newInstance(AUTHENTICATION_FOR_PASSPHRASE, getContext());
                break;
            case org.qtum.wallet.R.string.smart_contracts:
                fragment = SmartContractsFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.subscribe_tokens:
                fragment = SubscribeTokensFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.about:
                fragment = AboutFragment.newInstance(getContext());
                break;
            case org.qtum.wallet.R.string.log_out:
                startDialogFragmentForResult();
                break;
            case org.qtum.wallet.R.string.switch_theme:
                ThemeUtils.switchPreferencesTheme(getContext());
                getMainActivity().reloadActivity();
                break;
            default:
                break;
        }

        if (fragment != null) {
            openFragment(fragment);
        }
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        prefList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().removeLanguageListener(mLanguageChangeListener);
    }

    @Override
    public void onSwitchChange(int key, boolean isChecked) {
        switch (key) {
            case org.qtum.wallet.R.string.touch_id:
                getPresenter().onTouchIdSwitched(isChecked);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick() {
        getPresenter().clearWallet();
        getMainActivity().onLogout();
        mUpdateService = getMainActivity().getUpdateService();
        mUpdateService.stopMonitoring();
        BaseFragment startPageFragment = StartPageFragment.newInstance(false, getContext());
        getMainActivity().openRootFragment(startPageFragment);
    }

    @Override
    public void startDialogFragmentForResult() {
        LogOutDialogFragment logOutDialogFragment = new LogOutDialogFragment();
        logOutDialogFragment.setTargetFragment(this, 200);
        logOutDialogFragment.show(getFragmentManager(), LogOutDialogFragment.class.getCanonicalName());
    }

    @Override
    public boolean checkAvailabilityTouchId() {
        return getMainActivity().checkAvailabilityTouchId();
    }
}
