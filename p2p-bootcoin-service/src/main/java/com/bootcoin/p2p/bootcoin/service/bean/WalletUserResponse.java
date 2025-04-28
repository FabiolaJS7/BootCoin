package com.bootcoin.p2p.bootcoin.service.bean;

import com.bootcoin.p2p.bootcoin.service.bean.user.UserResponse;
import com.bootcoin.p2p.bootcoin.service.bean.wallet.WalletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletUserResponse {

    WalletResponse walletResponse;
    UserResponse userResponse;
}
