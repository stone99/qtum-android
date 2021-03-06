package org.qtum.wallet.ui.fragment.contract_function_fragment;

import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public interface ContractFunctionInteractor {
    List<ContractMethod> getContractMethod(String contractTemplateUiid);

    double getFeePerKbDouble();

    int getMinGasPrice();

    Observable<ContractFunctionInteractorImpl.CallSmartContractRespWrapper> callSmartContractObservable(String methodName,
                                                                                                        List<ContractMethodParameter> contractMethodParameterList,
                                                                                                        String contractAddress);

    BigDecimal getFeePerKbValue();

    Observable<List<UnspentOutput>> unspentOutputsForSeveralAddrObservable();

    String createTransactionHash(String abiParams, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, BigDecimal feePerKb, String fee, final String contractAddress);

    Observable<SendRawTransactionResponse> sendRawTransactionObservable(String code);
}
