package com.bootcoin.wallet.bootcoin.api.mapper;

import com.bootcoin.wallet.bootcoin.api.bean.WalletRequest;
import com.bootcoin.wallet.bootcoin.api.bean.WalletResponse;
import com.bootcoin.wallet.bootcoin.api.model.WalletModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    WalletModel getWalletModelFromWalletRequest(WalletRequest walletRequest);
    WalletResponse getWalletResponseFromWalletModel(WalletModel walletModel);
}
