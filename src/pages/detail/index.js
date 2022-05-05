import request from "@/api/request";
import Navigator from "@components/navigator";
import { useEffect, useState } from "react";
import { useLocation } from "react-router";

import { FixedUploadBtn } from "@components/common/button";

const Detail = (props) => {
  const [, pathname] = useLocation().pathname.split("/");
  const [videoInfo, setVideoInto] = useState(null);

  const fetchData = async () => {
    try {
      const data = await request(`/api/videos/${pathname}`, "get");
      setVideoInto(data);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <section>
      <Navigator />

      <div>
        <div>
          <div>
            <img src={videoInfo?.profileImgUrl} alt="프로필 이미지" />
            <div>
              <span>{videoInfo?.nickname}</span>
            </div>
          </div>
          <div></div>
        </div>
        <div>
          <div>{videoInfo?.content}</div>
          <div>{videoInfo?.createdDate}</div>
        </div>
        <div>
          <video>
            <source src={videoInfo?.videoUrl} />
          </video>
        </div>
      </div>
      <FixedUploadBtn />
    </section>
  );
};

export default Detail;
