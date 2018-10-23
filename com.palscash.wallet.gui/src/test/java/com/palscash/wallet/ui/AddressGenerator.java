package com.palscash.wallet.ui;

import com.palscash.common.crypto.Curves;
import com.palscash.common.crypto.PalsCashKeyPair;

public class AddressGenerator {

	public static void main(String[] args) {

		for (int i = 0; i < 100; i++) {
			PalsCashKeyPair kp = PalsCashKeyPair.createRandom(Curves.DEFAULT_CURVE);
			System.out.println(kp.getPrivateKeyAsBase58() + " : " + kp.getPalsCashAccount().getUuid());
			/*

5zDtgAZFX6UX4Mtyie8woguwWT4BeVAUHbhkTBSNS8Jc : pcax272hbgDSjVyX1LkrYAqLx4fqCjo6wn79
1Df38dB6uSS8AsaASghFXgtFgMA2bzb1ejEe9ACUHj7TW : pcax272LytRBm5zDUsXn1zQEwfiwztWzst61
1AfzVFVWEsY6DFw6d412FMgNzxvx8FS4S3Ru4NGBBi8PZ : pcax272vs4qgh7aSdhhce231QJEj9PE7HGx1
8ivMKMteGpyf96dUn1hu8aB2hYYnkEC65cYcQFqrW4Zb : pcax272wAaEa8S47FqzBAnrjq4AcNF278A6d
1CyDtWqeMj5oFk62PrewRzi3dDo89MS71UBeTQSCjscN7 : pcax272AT4TV8SV9JdVozUK9G2cNDBUGNLdd
KgSPYgTKttJYbjcC9gGES7KbEBBK6cft5oSAf65fL9s : pcax273G1dKVs5YUfCSo9aYwDTryCGxooz66
26R3YuthxYdmSinAZifhuFzJPvxwWNxibdTu99nLnxWP : pcax27vVFcjaNwKxykShcCFrQMc5N67Nix9
9b5xNPb7ekaG75AnJyidjF21HfKV8odpvKkVVqPgMNAX : pcax27qA7Q5pHdb7Y7xCNpTrK5DcNbyU79d
1CEGLRkDZjqqgi6n3nu31Vw9WVn7okYn6AZ37D3wshK3f : pcax273QgGQyU1mKA6LrgiGHURtA47TRkD13
7BT4FWLuL2PD9UJ6i3zdUfELtn5HuVQKrsbtvHWS5mtm : pcax272DPr8tzCNyNZQDv4HqBhKZhitCs242

			 */
		}
	}
}
