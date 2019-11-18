## OWNER
> url: http://oreh.onyah.net:7080

#### 1. 사장 로그인(GET): getOwnerUser(owner_id, password)
> /owner/login?id={id}&passwd={passwd}

#### 2. 사장 회원가입(POST): addOwnerUser(name, owner_id, password, phone)
> /owner/login?id={id}&passwd={passwd}&name={name}&phone={phone}

#### 3. 사장 소유 가게 이름 출력(GET): getOwnerShop(owner_id)
> /ownShop?id={owner_id}

#### 4. 새로운 가게 등록(POST): addOwnerShop(owner_id, name, tel, addr, category, table)
> /ownShop?id={owner_id}&name={shop_name}&tel={tel}&addr={addr}&category={category}&table={Y/N}

#### 5. 새로운 가게 테이블 등록(POST): addOwnerShopTable(number, count)
> /ownShop/add/table?number={table_number}&count={table_count}

#### 6. 가게 삭제(POST)(가게 메뉴, 좋아요 가게, 블랙리스트, 가게 테이블 정보 삭제): deleteOwnerShop(shop_id)
> /ownShop/delete?shop={shop_id}

#### 7. 가게 메뉴 목록 확인(GET): getMenuwithTimeSale(shop_id, time)
> /ownMenu?id={shop_id}&time={current time:HH}

#### 8. 해당 가게 메뉴 추가 (POST): addShopMenu(shop_id, name, price, des, count)
> /ownMenu/add?id={shop_id}&name={menu_name}&price={price}&des={description}&count={count}

#### 9. 해당 가게 메뉴 삭제(POST): deleteShopMenu(shop_id, name)
> /ownMenu/del?id={shop_id}&name={menu_name}

#### 10. 블랙리스트 모두 출력(GET): getBlackList()
> /blacklist/all

#### 11. 블랙리스트 추가(POST): addBlackList(client_id, shop_id, comment)
> /blacklist/add?id={client_id}&shop={shop_id}&comment={comment}

#### 12. 마이페이지 사장정보 출력(GET): getOwnerUserDetail(owner_id)
> /mypage/owner?id={owner_id}

#### 13. 마이페이지 사장비밀번호만 변경(POST): updateOwnerUser(owner_id, new_passwd)
> /mypage/owner?id={owner_id}&new={new_passwd}

#### 14. 가게별 자리 예약 정보 출력(GET): getOwnerReservationTable(shop_id)
> /reservation/table/owner?shop={shop_id}

#### 15. 가게별 번호표 예약 정보 출력(GET): getOwnerReservationMenu(shop_id)
> /reservation/owner?shop={shop_id}

#### 16. 타임세일 내역 출력(GET): getTimeSale(shop_id)
> /timesale?shop={shop_id}

#### 17. 타임세일 등록(POST): addTimeSale(shop_id, name, sale_price, time)
> /timesale/add?shop={shop_id}&menu={menu_name}&price={menu_salePrice}&saletime={menu_saleTime}

#### 18. 타임세일 삭제(POST): deleteTimeSale(id)
> /timesale/delete?register={timesale_id}
