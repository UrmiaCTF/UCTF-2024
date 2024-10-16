# Flag Seller

# Description
Step into the world of "Flag Seller," where the quest for flags begins! 🕵️‍♂️ Your mission is to visit [https://flag-seller.uctf.ir](https://flag-seller.uctf.ir) to uncover the hidden flags. To gain access, you'll need to generate an access token from the CTFd panel. For a guide on how to create your token, check out the [documentation](https://docs.ctfd.io/docs/api/getting-started/#access-token). Good luck, and may your search be fruitful! 🌟


# Write Up / Video

N.A.

# Flag

```
uctf{YeralmaKababiIsTheBestProduct}
```
# Categories

Check the categories which the challenge belongs to.

- [X] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Blockchain
- [ ] Steganography
- [ ] AI
- [ ] Data Science

# Points

| Warm up | This Challenge | Evil |
| ------- |:--------------:| ----:|
| 25      |      300       | 500  |

# Resources

#### Deployment

this application needs 2 subdomain name
- flag-seller.uctf.ir
- blupay.uctf.ir

and there is an authentication base on `CTFD`: players needs to create token on ctfd then base on that and team name they can log in
this method needs to configure env `ctfd.url=https://ctfd.uctf.ir` this is applied on docker compose.

----
> Build image
> > script generate the image `flag-seller:dev`
```shell
# in linux server inside project folder
./build.sh
```

> Deploy
> > `docker-compose.yml` will provide you to deploy the image 
