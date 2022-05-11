import styled from "styled-components";
import React, { useEffect, useState } from "react";

import VideoList from "@components/videoList";
import Navigator from "@components/navigator";

import noInterests from "./assets/noInterests.svg";
import TitlePrevHeader from "@/components/common/titlePrevHeader";
import { useDispatch } from "react-redux";
import { changeNavigator } from "@/redux/reducers/navigator";
import request from "@/api/request";
import { useSelector } from "react-redux";

const Interests = () => {
  const { email } = useSelector(({ user }) => user.user);
  const [videoList, setVideoList] = useState([]);
  const dispatch = useDispatch();

  const fetchData = async () => {
    try {
      const result = await request("/api/hearts", "get", { userEmail: email });
      setVideoList(result);
    } catch (e) {}
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    dispatch(changeNavigator("liked"));
  }, []);

  return (
    <InterestBlock>
      <TitlePrevHeader title={"관심영상"} background={"white"} />
      <div className="contents">
        <h3>
          마음에 드는 영상에 하트를 누르고
          <br /> 관심영상을 업데이트해보세요!
        </h3>
        {videoList.length !== 0 ? (
          <VideoList videos={videoList} type={"interest"} />
        ) : (
          <NoVideoList background={noInterests} />
        )}
      </div>
      <Navigator />
    </InterestBlock>
  );
};
export default Interests;

const InterestBlock = styled.div`
  width: 100%;
  height: 100%;

  .top {
    padding: 53px 30px 0 30px;
    .top-display {
      display: flex;
      justify-content: space-between;
      span {
        font-size: 18px;
      }
    }
  }
  .contents {
    h3 {
      color: #979797;
      text-align: center;
      font-size: 14px;
      margin-top: 27px;
      line-height: 19px;
    }
    img {
      display: block;
      margin: auto;
      text-align: center;
    } //이미지 중앙정렬이 안 먹음
  }
`;

const NoVideoList = styled.div`
  margin: 100px auto 0 auto;

  width: 80px;
  height: 135px;
  background-image: url(${(props) => props.background});
  background-repeat: no-repeat;
  bakcground-size: cover;
`;
